package com.fishman.welder_management_backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingMachinesMapper;
import com.fishman.welder_management_backend.model.domain.*;
import com.fishman.welder_management_backend.model.request.MachineAddRequest;
import com.fishman.welder_management_backend.model.request.MachineUpdateRequest;
import com.fishman.welder_management_backend.model.vo.UserVO;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;
import com.fishman.welder_management_backend.properties.FishmanProperties;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingMachinesService;
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
* @description 针对表【weldingmachines】的数据库操作Service实现
* @createDate 2024-05-06 19:01:09
*/
@Service
public class WeldingMachinesServiceImpl extends ServiceImpl<WeldingMachinesMapper, WeldingMachine>
    implements WeldingMachinesService {

    @Resource
    FishmanProperties fishmanProperties;

    @Resource
    UserService userService;

    @Value("${server.servlet.session.cookie.domain}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${fishman.qiniu.url:null}")
    private String qiniuUrl;

    /**
     * @param machineAddRequest 设备添加请求
     * @param loginUser      登录用户
     * @return
     */
    @Override
    public Long addMachine(MachineAddRequest machineAddRequest, User loginUser) {
        WeldingMachine machine = new WeldingMachine();
        ArrayList<String> imageNameList=new ArrayList<>();
        try {
            MultipartFile[] images = machineAddRequest.getImagePath();
            if (images!= null && images.length > 0){
                if(fishmanProperties.isUseLocalStorage()){
                    for (MultipartFile image : images) {
                        String filename = FileUtils.uploadFile2Local(image);
                        imageNameList.add(filename);
                    }
                }
                else {
                    for (MultipartFile image : images) {
                        String filename = FileUtils.uploadFile2Local(image);
                        imageNameList.add(filename);
                    }
                }
                String imagePath = StringUtils.join(imageNameList, ",");
                machine.setImagePath(imagePath);
            }
        }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }
        //todo 校验参数合法性
        machine.setMachineName(machineAddRequest.getMachineName());
        machine.setSerialNumber(machineAddRequest.getSerialNumber());
        return machine.getId();
    }

    /**
     * 管理员获取分页
     * @param currentPage 当前页码
     * @param id          id
     * @return
     */
    //todo 鉴权
    @Override
    public Page<WeldingMachineVO> listMyMachines(long currentPage, Long id) {
        if (currentPage <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<WeldingMachine> machineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        machineLambdaQueryWrapper.eq(WeldingMachine::getId, id);
        Page<WeldingMachine> machinePage = this.page(new Page<>(currentPage, PAGE_SIZE), machineLambdaQueryWrapper);
        Page<WeldingMachineVO> machineVoPage = new Page<>();
        BeanUtils.copyProperties(machinePage, machineVoPage);
        List<WeldingMachineVO> weldingMachineVOList = machinePage.getRecords().stream().map((wellingMachines) -> {
            WeldingMachineVO weldingMachineVO = new WeldingMachineVO();
            BeanUtils.copyProperties(wellingMachines, weldingMachineVO);
            return weldingMachineVO;
        }).collect(Collectors.toList());
        List<WeldingMachineVO> machineWithCoverImg = getCoverImg(weldingMachineVOList);
        machineVoPage.setRecords(machineWithCoverImg);
        return machineVoPage;

    }

    @Override
    public Page<WeldingMachineVO> pageWeldingMachine(long currentPage, String machineName, Long id) {
        LambdaQueryWrapper<WeldingMachine> weldingMachineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        weldingMachineLambdaQueryWrapper.like(StringUtils.isNotBlank(machineName), WeldingMachine::getMachineName, machineName);
        weldingMachineLambdaQueryWrapper.orderBy(true, false, WeldingMachine::getCreateTime);
        Page<WeldingMachine> weldingMachinePage = this.page(new Page<>(currentPage, PAGE_SIZE), weldingMachineLambdaQueryWrapper);
        Page<WeldingMachineVO> weldingMachineVoPage = new Page<>();
        BeanUtils.copyProperties(weldingMachinePage, weldingMachineVoPage);
        List<WeldingMachineVO> weldingMachineVOList = weldingMachinePage.getRecords().stream().map((weldingMachine) -> {
            WeldingMachineVO weldingMachineVO = new WeldingMachineVO();
            BeanUtils.copyProperties(weldingMachine, weldingMachineVO);
            return weldingMachineVO;
        }).collect(Collectors.toList());
        List<WeldingMachineVO> blogWithCoverImg = getCoverImg(weldingMachineVOList);
        weldingMachineVoPage.setRecords(blogWithCoverImg);
        return weldingMachineVoPage;
    }

    /**
     * 获取封面img
     *
     * @param machineVOList 设备volist
     * @return {@link List}<{@link WeldingMachineVO}>
     */
    private List<WeldingMachineVO> getCoverImg(List<WeldingMachineVO> machineVOList) {
        for (WeldingMachineVO weldingMachineVO : machineVOList) {
            String images = weldingMachineVO.getImages();
            if (images == null) {
                continue;
            }
            String[] imgStr = images.split(",");
            if (fishmanProperties.isUseLocalStorage()) {
                String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + imgStr[0];
                weldingMachineVO.setImagePath(fileUrl);
            } else {
                weldingMachineVO.setImagePath(qiniuUrl + imgStr[0]);
            }
        }
        return machineVOList;
    }

    /**
     * 获取设备详细信息
     * @param machineId 设备id
     * @param userId 用户id
     * @return
     */
    @Override
    public WeldingMachineVO getMachineById(long machineId, Long userId) {
        WeldingMachine weldingMachine = this.getById(machineId);
        if (weldingMachine == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法找到该设备");
        }
        WeldingMachineVO wellingMachineVO = new WeldingMachineVO();
        BeanUtils.copyProperties(weldingMachine, wellingMachineVO);
        UserVO authorVO = new UserVO();
        User author = userService.getById(weldingMachine.getId());
        BeanUtils.copyProperties(author, authorVO);
        String images = wellingMachineVO.getImages();
        if (images == null) {
            return wellingMachineVO;
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
        wellingMachineVO.setImages(imgStr);
        wellingMachineVO.setImagePath(imgStrList.get(0));
        return wellingMachineVO;
    }

    @Override
    public void deleteMachine(Long MachineId, Long userId, boolean isAdmin) {
        this.removeById(MachineId);
    }

    @Override
    public void updateMachine(MachineUpdateRequest machineUpdateRequest, Long userId, boolean isAdmin) {
        if (machineUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String machineName = machineUpdateRequest.getMachineName();
        String tags = machineUpdateRequest.getTags();
        if (StringUtils.isAnyBlank(machineName, tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingMachine wellingMachine = new WeldingMachine();
        wellingMachine.setId(machineUpdateRequest.getId());
        ArrayList<String> imageNameList = new ArrayList<>();
        if (StringUtils.isNotBlank(machineUpdateRequest.getImgStr())) {
            String imgStr = machineUpdateRequest.getImgStr();
            String[] imgs = imgStr.split(",");
            for (String img : imgs) {
                String fileName = img.substring(img.lastIndexOf("/") + 1);
                imageNameList.add(fileName);
            }
        }
        if (machineUpdateRequest.getImagePath() != null) {
            MultipartFile[] images = machineUpdateRequest.getImagePath();
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
            wellingMachine.setImagePath(imageStr);
        }
        wellingMachine.setMachineName(machineUpdateRequest.getMachineName());
        wellingMachine.setTags(machineUpdateRequest.getTags());
        this.updateById(wellingMachine);
    }

}




