package com.fishman.welder_management_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingMachineFaultsMapper;
import com.fishman.welder_management_backend.model.domain.*;
import com.fishman.welder_management_backend.model.request.FaultsAddRequest;
import com.fishman.welder_management_backend.model.request.FaultsUpdateRequest;
import com.fishman.welder_management_backend.model.vo.WeldingMachineFaultsVO;
import com.fishman.welder_management_backend.properties.FishmanProperties;
import com.fishman.welder_management_backend.service.WeldingMachineFaultsService;
import com.fishman.welder_management_backend.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static com.fishman.welder_management_backend.contant.SystemConstants.PAGE_SIZE;


/**
* @author fishman
* @description 针对表【weldingmachinefaults】的数据库操作Service实现
* @createDate 2024-05-06 19:01:07
*/
@Service
public class WeldingMachineFaultsServiceImpl extends ServiceImpl<WeldingMachineFaultsMapper, WeldingMachineFaults>
    implements WeldingMachineFaultsService {

    @Resource
    private FishmanProperties fishmanProperties;

    @Value("${fishman.qiniu.url:null}")
    private String qiniuUrl;

    @Value("${server.servlet.session.cookie.domain}")
    private String host;

    @Value("${server.port}")
    private String port;
    @Override
    public Long addFaults(FaultsAddRequest faultsAddRequest, User loginUser) {
        WeldingMachineFaults weldingMachineFaults =new WeldingMachineFaults();
        ArrayList<String> imageNameList = new ArrayList<>();
        try {
            MultipartFile[] images = faultsAddRequest.getImages();
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
                weldingMachineFaults.setImagePath(imageStr);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        weldingMachineFaults.setTitle(faultsAddRequest.getTitle());
        weldingMachineFaults.setDescription(faultsAddRequest.getDescription());
        return weldingMachineFaults.getId();
    }

    @Override
    public Page<WeldingMachineFaultsVO> listMyFaults(long currentPage, Long id) {
        if (currentPage <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<WeldingMachineFaults> weldingMachineFaultsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        weldingMachineFaultsLambdaQueryWrapper.eq(WeldingMachineFaults::getEmployeeID, id);
        Page<WeldingMachineFaults> weldingMachineFaultsPage = this.page(new Page<>(currentPage, PAGE_SIZE), weldingMachineFaultsLambdaQueryWrapper);
        Page<WeldingMachineFaultsVO> weldingMachineFaultsVoPage = new Page<>();
        BeanUtils.copyProperties(weldingMachineFaultsPage, weldingMachineFaultsVoPage);
        List<WeldingMachineFaultsVO> faultsVOList = weldingMachineFaultsPage.getRecords().stream().map((weldingMachineFaults) -> {
            WeldingMachineFaultsVO weldingMachineFaultsVO = new WeldingMachineFaultsVO();
            BeanUtils.copyProperties(weldingMachineFaults, weldingMachineFaultsVO);
            return weldingMachineFaultsVO;
        }).collect(Collectors.toList());
        List<WeldingMachineFaultsVO> faultsWithCoverImg = getCoverImg(faultsVOList);
        weldingMachineFaultsVoPage.setRecords(faultsWithCoverImg);
        return weldingMachineFaultsVoPage;
    }


    @Override
    public Page<WeldingMachineFaultsVO> pageFaults(long currentPage, String title, Long userId) {
        LambdaQueryWrapper<WeldingMachineFaults> weldingMachineFaultsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        weldingMachineFaultsLambdaQueryWrapper.like(StringUtils.isNotBlank(title), WeldingMachineFaults::getTitle, title);
        weldingMachineFaultsLambdaQueryWrapper.orderBy(true, false, WeldingMachineFaults::getCreateTime);
        Page<WeldingMachineFaults> faultsPage = this.page(new Page<>(currentPage, PAGE_SIZE), weldingMachineFaultsLambdaQueryWrapper);
        Page<WeldingMachineFaultsVO> faultsVoPage = new Page<>();
        BeanUtils.copyProperties(faultsPage, faultsVoPage);
        List<WeldingMachineFaultsVO> faultsVOList = faultsPage.getRecords().stream().map((weldingMachineFaults) -> {
            WeldingMachineFaultsVO weldingMachineFaultsVO = new WeldingMachineFaultsVO();
            BeanUtils.copyProperties(weldingMachineFaults, weldingMachineFaultsVO);
            return weldingMachineFaultsVO;
        }).collect(Collectors.toList());
        List<WeldingMachineFaultsVO> faultsWithCoverImg = getCoverImg(faultsVOList);
        faultsVoPage.setRecords(faultsWithCoverImg);
        return faultsVoPage;
    }

    @Override
    public WeldingMachineFaultsVO getFaultsById(long faultsId, Long userId) {
        WeldingMachineFaults weldingMachineFaults = this.getById(faultsId);
        if (weldingMachineFaults == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法找到该博文");
        }
        WeldingMachineFaultsVO weldingMachineFaultsVO = new WeldingMachineFaultsVO();
        BeanUtils.copyProperties(weldingMachineFaults, weldingMachineFaultsVO);
        String images = weldingMachineFaultsVO.getImages();
        if (images == null) {
            return weldingMachineFaultsVO;
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
        weldingMachineFaultsVO.setImages(imgStr);
        weldingMachineFaultsVO.setCoverImage(imgStrList.get(0));
        return weldingMachineFaultsVO;
    }

    @Override
    public void deleteFaults(Long faultsId, Long userId, boolean isAdmin) {
        if (isAdmin) {
            this.removeById(faultsId);
            return;
        }
        WeldingMachineFaults weldingMachineFaults = this.getById(faultsId);
        if (!userId.equals(weldingMachineFaults.getEmployeeID())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        this.removeById(faultsId);
    }

    @Override
    public void updateFaults(FaultsUpdateRequest faultsUpdateRequest, Long userId, boolean isAdmin) {
        if (faultsUpdateRequest.getMachineID() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long createUserId = this.getById(faultsUpdateRequest.getMachineID()).getEmployeeID();
        if (!createUserId.equals(userId) && !isAdmin) {
            throw new BusinessException(ErrorCode.NO_AUTH, "没有权限");
        }
        String title = faultsUpdateRequest.getTitle();
        String content = faultsUpdateRequest.getDescription();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingMachineFaults weldingMachineFaults = new WeldingMachineFaults();
        weldingMachineFaults.setId(faultsUpdateRequest.getMachineID());
        ArrayList<String> imageNameList = new ArrayList<>();
        if (StringUtils.isNotBlank(faultsUpdateRequest.getImgStr())) {
            String imgStr = faultsUpdateRequest.getImgStr();
            String[] imgs = imgStr.split(",");
            for (String img : imgs) {
                String fileName = img.substring(img.lastIndexOf("/") + 1);
                imageNameList.add(fileName);
            }
        }
        if (faultsUpdateRequest.getImages() != null) {
            MultipartFile[] images = faultsUpdateRequest.getImages();
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
            weldingMachineFaults.setImagePath(imageStr);
        }
        weldingMachineFaults.setTitle(faultsUpdateRequest.getTitle());
        weldingMachineFaults.setDescription(faultsUpdateRequest.getDescription());
        this.updateById(weldingMachineFaults);
    }

    /**
     * 获取封面img
     *
     * @param faultsVOList 故障报告volist
     */
    private List<WeldingMachineFaultsVO> getCoverImg(List<WeldingMachineFaultsVO> faultsVOList) {
        for (WeldingMachineFaultsVO weldingMachineFaultsVO : faultsVOList) {
            String images = weldingMachineFaultsVO.getImages();
            if (images == null) {
                continue;
            }
            String[] imgStr = images.split(",");
            if (fishmanProperties.isUseLocalStorage()) {
                String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + imgStr[0];
                weldingMachineFaultsVO.setCoverImage(fileUrl);
            } else {
                weldingMachineFaultsVO.setCoverImage(qiniuUrl + imgStr[0]);
            }
        }
        return faultsVOList;
    }

}




