package com.fishman.usercenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fishman.usercenter.mapper.BlogLikeMapper;
import com.fishman.usercenter.model.domain.BlogLike;
import com.fishman.usercenter.service.BlogLikeService;
import org.springframework.stereotype.Service;

/**
 * @author fishman
 * @description 针对表【blog_like】的数据库操作Service实现
 * @createDate 2023-06-05 21:54:55
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike>
        implements BlogLikeService {

}




