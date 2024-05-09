package com.fishman.welder_management_backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingMachinesMapper;
import com.fishman.welder_management_backend.model.domain.*;
import com.fishman.welder_management_backend.model.enums.MachineStatusEnum;
import com.fishman.welder_management_backend.model.request.MachineAddRequest;
import com.fishman.welder_management_backend.model.request.MachineUpdateRequest;
import com.fishman.welder_management_backend.model.vo.UserVO;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;
import com.fishman.welder_management_backend.properties.FishmanProperties;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingMachinesService;
import com.fishman.welder_management_backend.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
     * @param weldingMachine 设备添加请求
     * @param loginUser      登录用户
     * @return
     */
    @Override
    public long addMachine(WeldingMachine weldingMachine, User loginUser) {
        //todo 校验参数合法性
        if (weldingMachine == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        final long userId = loginUser.getId();
        weldingMachine.setId(null);
        weldingMachine.setUserId(userId);
        boolean result = this.save(weldingMachine);
        Long machineId = weldingMachine.getId();
        if (!result|| machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return machineId;
    }
    /**
     * 收到用户标签
     *
     * @param machineId id
     */
    @Override
    public List<String> getMachineTags(Long machineId) {
        WeldingMachine weldingMachine = this.getById(machineId);
        String machineTags = weldingMachine.getTags();
        Gson gson = new Gson();
        return gson.fromJson(machineTags, new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * 更新标记
     *
     * @param tags   标签
     * @param machineId 用户id
     */
    @Override
    public void updateMachineTags(List<String> tags, Long machineId) {
        WeldingMachine weldingMachine = new WeldingMachine();
        Gson gson = new Gson();
        String tagsJson = gson.toJson(tags);
        weldingMachine.setId(machineId);
        weldingMachine.setTags(tagsJson);
        this.updateById(weldingMachine);
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
        WeldingMachineVO weldingMachineVO = new WeldingMachineVO();
        BeanUtils.copyProperties(weldingMachine, weldingMachineVO);
        UserVO authorVO = new UserVO();
        User author = userService.getById(weldingMachine.getId());
        BeanUtils.copyProperties(author, authorVO);
        String images = weldingMachineVO.getImages();
        if (images == null) {
            return weldingMachineVO;
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
        weldingMachineVO.setImages(imgStr);
        weldingMachineVO.setImagePath(imgStrList.get(0));
        return weldingMachineVO;
    }

    @Override
    public void deleteMachine(Long MachineId, Long userId, boolean isAdmin) {
        this.removeById(MachineId);
    }

    @Override
    public boolean updateMachine(MachineUpdateRequest machineUpdateRequest, Long userId, boolean isAdmin) {
        if (machineUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long machineId = machineUpdateRequest.getId();
        if (machineId == null|| machineId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingMachine weldingMachine = this.getById(machineId);
        if (weldingMachine == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法找到该设备");
        }
        String machineName = machineUpdateRequest.getMachineName();
        String tags = machineUpdateRequest.getTags();
        if (StringUtils.isAnyBlank(machineName, tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingMachine updateMachine = new WeldingMachine();
        BeanUtils.copyProperties(machineUpdateRequest, updateMachine);
       return this.updateById(updateMachine);
    }

    /**
     * 获取我创建的设备
     * @param currentPage
     * @param userId
     * @return
     */
    @Override
    public Page<WeldingMachineVO> listMyCreate(long currentPage, Long userId) {
        LambdaQueryWrapper<WeldingMachine> machineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        machineLambdaQueryWrapper.eq(WeldingMachine::getId, userId);
        Page<WeldingMachine> machienPage = this.page(new Page<>(currentPage, PAGE_SIZE), machineLambdaQueryWrapper);
        List<WeldingMachineVO> machienVOList = machienPage.getRecords()
                .stream().map((weldingMachine) -> this.getMachineById(weldingMachine.getId(), userId))
                .collect(Collectors.toList());
        Page<WeldingMachineVO> machineVOPage = new Page<>();
        BeanUtils.copyProperties(machienPage, machineVOPage);
        machineVOPage.setRecords(machienVOList);
        return machineVOPage;
    }


}




