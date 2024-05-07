package com.fishman.welder_management_backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingMachineFaultsMapper;
import com.fishman.welder_management_backend.model.domain.*;
import com.fishman.welder_management_backend.model.enums.MessageTypeEnum;
import com.fishman.welder_management_backend.model.request.BlogAddRequest;
import com.fishman.welder_management_backend.model.request.BlogUpdateRequest;
import com.fishman.welder_management_backend.model.vo.BlogVO;
import com.fishman.welder_management_backend.model.vo.UserVO;
import com.fishman.welder_management_backend.service.WeldingMachineFaultsService;
import com.fishman.welder_management_backend.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fishman.welder_management_backend.contant.RedisConstants.MESSAGE_LIKE_NUM_KEY;
import static com.fishman.welder_management_backend.contant.RedissonConstant.BLOG_LIKE_LOCK;
import static com.fishman.welder_management_backend.contant.RedissonConstant.DEFAULT_LEASE_TIME;
import static com.fishman.welder_management_backend.contant.SystemConstants.PAGE_SIZE;
import static sun.awt.SunToolkit.DEFAULT_WAIT_TIME;

/**
* @author fishman
* @description 针对表【weldingmachinefaults】的数据库操作Service实现
* @createDate 2024-05-06 19:01:07
*/
@Service
public class WeldingMachineFaultsServiceImpl extends ServiceImpl<WeldingMachineFaultsMapper, WeldingMachineFaults>
    implements WeldingMachineFaultsService {
    @Override
    public Long addBlog(BlogAddRequest blogAddRequest, User loginUser) {
        Blog blog = new Blog();
        ArrayList<String> imageNameList = new ArrayList<>();
        try {
            MultipartFile[] images = blogAddRequest.getImages();
            if (images != null) {
                if (fishmanProperties.isUseLocalStorage()) {
                    for (MultipartFile image : images) {
                        String filename = FileUtils.uploadFile2Local(image);
                        imageNameList.add(filename);
                    }
                } else {
                    for (MultipartFile image : images) {
                        String filename = FileUtils.uploadFile2Cloud(image);
                        imageNameList.add(filename);
                    }
                }
                String imageStr = StringUtils.join(imageNameList, ",");
                blog.setImages(imageStr);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        blog.setUserId(loginUser.getId());
        blog.setTitle(blogAddRequest.getTitle());
        blog.setContent(blogAddRequest.getContent());
        return blog.getId();
    }

    @Override
    public Page<BlogVO> listMyBlogs(long currentPage, Long id) {
        if (currentPage <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.eq(Blog::getUserId, id);
        Page<Blog> blogPage = this.page(new Page<>(currentPage, PAGE_SIZE), blogLambdaQueryWrapper);
        Page<BlogVO> blogVoPage = new Page<>();
        BeanUtils.copyProperties(blogPage, blogVoPage);
        List<BlogVO> blogVOList = blogPage.getRecords().stream().map((blog) -> {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            return blogVO;
        }).collect(Collectors.toList());
        List<BlogVO> blogWithCoverImg = getCoverImg(blogVOList);
        blogVoPage.setRecords(blogWithCoverImg);
        return blogVoPage;
    }

    /**
     * 点赞博文
     *
     * @param blogId 博文id
     * @param userId 用户id
     */
    @Override
    public void likeBlog(long blogId, Long userId) {
        RLock lock = redissonClient.getLock(BLOG_LIKE_LOCK + blogId + ":" + userId);
        try {
            if (lock.tryLock(DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                Blog blog = this.getById(blogId);
                if (blog == null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "博文不存在");
                }
                LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blogId);
                blogLikeLambdaQueryWrapper.eq(BlogLike::getUserId, userId);
                long isLike = blogLikeService.count(blogLikeLambdaQueryWrapper);
                if (isLike > 0) {
                    blogLikeService.remove(blogLikeLambdaQueryWrapper);
                    doUnLikeBlog(blog, userId);
                } else {
                    doLikeBlog(blog, userId);
                }
            }
        } catch (Exception e) {
            log.error("LikeBlog error", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 取消点赞博文
     *
     * @param blog   博文
     * @param userId 用户id
     */
    public void doUnLikeBlog(Blog blog, Long userId) {
        int newNum = blog.getLikedNum() - 1;
        this.update().eq("id", blog.getId()).set("likedNum", newNum).update();
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>();
        messageQueryWrapper
                .eq(Message::getType, MessageTypeEnum.BLOG_LIKE.getValue())
                .eq(Message::getFromId, userId)
                .eq(Message::getToId, blog.getUserId())
                .eq(Message::getData, String.valueOf(blog.getId()));
        messageService.remove(messageQueryWrapper);
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + blog.getUserId();
        String upNumStr = stringRedisTemplate.opsForValue().get(likeNumKey);
        if (!StrUtil.isNullOrUndefined(upNumStr) && Long.parseLong(upNumStr) != 0) {
            stringRedisTemplate.opsForValue().decrement(likeNumKey);
        }
    }

    /**
     * 点赞博文
     *
     * @param blog   博文
     * @param userId 用户id
     */
    public void doLikeBlog(Blog blog, Long userId) {
        BlogLike blogLike = new BlogLike();
        blogLike.setBlogId(blog.getId());
        blogLike.setUserId(userId);
        blogLikeService.save(blogLike);
        int newNum = blog.getLikedNum() + 1;
        this.update().eq("id", blog.getId()).set("likedNum", newNum).update();
        Message message = new Message();
        message.setType(MessageTypeEnum.BLOG_LIKE.getValue());
        message.setFromId(userId);
        message.setToId(blog.getUserId());
        message.setData(String.valueOf(blog.getId()));
        messageService.save(message);
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + blog.getUserId();
        Boolean hasKey = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasKey)) {
            stringRedisTemplate.opsForValue().increment(likeNumKey);
        } else {
            stringRedisTemplate.opsForValue().set(likeNumKey, "1");
        }
    }

    @Override
    public Page<BlogVO> pageBlog(long currentPage, String title, Long userId) {
        LambdaQueryWrapper<Blog> blogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLambdaQueryWrapper.like(StringUtils.isNotBlank(title), Blog::getTitle, title);
        blogLambdaQueryWrapper.orderBy(true, false, Blog::getCreateTime);
        Page<Blog> blogPage = this.page(new Page<>(currentPage, PAGE_SIZE), blogLambdaQueryWrapper);
        Page<BlogVO> blogVoPage = new Page<>();
        BeanUtils.copyProperties(blogPage, blogVoPage);
        List<BlogVO> blogVOList = blogPage.getRecords().stream().map((blog) -> {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            if (userId != null) {
                LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blog.getId()).eq(BlogLike::getUserId, userId);
                long count = blogLikeService.count(blogLikeLambdaQueryWrapper);
                blogVO.setIsLike(count > 0);
            }
            return blogVO;
        }).collect(Collectors.toList());
        List<BlogVO> blogWithCoverImg = getCoverImg(blogVOList);
        blogVoPage.setRecords(blogWithCoverImg);
        return blogVoPage;
    }

    @Override
    public BlogVO getBlogById(long blogId, Long userId) {
        Blog blog = this.getById(blogId);
        if (blog == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法找到该博文");
        }
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);
        LambdaQueryWrapper<BlogLike> blogLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogLikeLambdaQueryWrapper.eq(BlogLike::getUserId, userId);
        blogLikeLambdaQueryWrapper.eq(BlogLike::getBlogId, blogId);
        long isLike = blogLikeService.count(blogLikeLambdaQueryWrapper);
        blogVO.setIsLike(isLike > 0);
        User author = userService.getById(blog.getUserId());
        if (author == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法找到该博文作者");
        }
        UserVO authorVO = new UserVO();
        BeanUtils.copyProperties(author, authorVO);
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, authorVO.getId()).eq(Follow::getUserId, userId);
        blogVO.setAuthor(authorVO);
        String images = blogVO.getImages();
        if (images == null) {
            return blogVO;
        }
        String[] imgStrs = images.split(",");
        ArrayList<String> imgStrList = new ArrayList<>();
        for (String imgStr : imgStrs) {
            if (fishmanProperties.isUseLocalStorage()) {
                String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + imgStr;
                imgStrList.add(fileUrl);
            } else {
                imgStrList.add(qiniuUrl + imgStr);
            }
        }
        String imgStr = StringUtils.join(imgStrList, ",");
        blogVO.setImages(imgStr);
        blogVO.setCoverImage(imgStrList.get(0));
        return blogVO;
    }

    @Override
    public void deleteBlog(Long blogId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            this.removeById(blogId);
            return;
        }
        Blog blog = this.getById(blogId);
        if (!userId.equals(blog.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        this.removeById(blogId);
    }

    @Override
    public void updateBlog(BlogUpdateRequest blogUpdateRequest, Long userId, boolean isAdmin) {
        if (blogUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long createUserId = this.getById(blogUpdateRequest.getId()).getUserId();
        if (!createUserId.equals(userId) && !isAdmin) {
            throw new BusinessException(ErrorCode.NO_AUTH, "没有权限");
        }
        String title = blogUpdateRequest.getTitle();
        String content = blogUpdateRequest.getContent();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Blog blog = new Blog();
        blog.setId(blogUpdateRequest.getId());
        ArrayList<String> imageNameList = new ArrayList<>();
        if (StringUtils.isNotBlank(blogUpdateRequest.getImgStr())) {
            String imgStr = blogUpdateRequest.getImgStr();
            String[] imgs = imgStr.split(",");
            for (String img : imgs) {
                String fileName = img.substring(img.lastIndexOf("/") + 1);
                imageNameList.add(fileName);
            }
        }
        if (blogUpdateRequest.getImages() != null) {
            MultipartFile[] images = blogUpdateRequest.getImages();
            if (fishmanProperties.isUseLocalStorage()) {
                for (MultipartFile image : images) {
                    String filename = FileUtils.uploadFile2Local(image);
                    imageNameList.add(filename);
                }
            } else {
                for (MultipartFile image : images) {
                    String filename = FileUtils.uploadFile2Cloud(image);
                    imageNameList.add(filename);
                }
            }
        }
        if (!imageNameList.isEmpty()) {
            String imageStr = StringUtils.join(imageNameList, ",");
            blog.setImages(imageStr);
        }
        blog.setTitle(blogUpdateRequest.getTitle());
        blog.setContent(blogUpdateRequest.getContent());
        this.updateById(blog);
    }

    /**
     * 获取封面img
     *
     * @param blogVOList 博客volist
     * @return {@link List}<{@link BlogVO}>
     */
    private List<BlogVO> getCoverImg(List<BlogVO> blogVOList) {
        for (BlogVO blogVO : blogVOList) {
            String images = blogVO.getImages();
            if (images == null) {
                continue;
            }
            String[] imgStr = images.split(",");
            if (fishmanProperties.isUseLocalStorage()) {
                String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + imgStr[0];
                blogVO.setCoverImage(fileUrl);
            } else {
                blogVO.setCoverImage(qiniuUrl + imgStr[0]);
            }
        }
        return blogVOList;
    }

}




