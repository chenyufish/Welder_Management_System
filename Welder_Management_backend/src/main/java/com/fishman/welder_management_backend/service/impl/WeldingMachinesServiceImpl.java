package com.fishman.welder_management_backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingMachinesMapper;
import com.fishman.welder_management_backend.model.domain.*;
import com.fishman.welder_management_backend.model.enums.MachineStatusEnum;
import com.fishman.welder_management_backend.model.enums.TeamStatusEnum;
import com.fishman.welder_management_backend.model.request.MachineAddRequest;
import com.fishman.welder_management_backend.model.request.MachineQueryRequest;
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

    @Override
    public Page<WeldingMachineVO> listMachines(long currentPage, MachineQueryRequest machineQuery, boolean isAdmin) {
        LambdaQueryWrapper<WeldingMachine> machineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 组合查询条件
        if (machineQuery != null) {
            Long id = machineQuery.getId();
            List<Long> idList = machineQuery.getIdList();
            String searchText = machineQuery.getSearchText();
            // 根据队伍名查询
            String name = machineQuery.getMachineName();
            // 根据描述查询
            String description = machineQuery.getDescription();
            // 根据队长Id查询
            Long userId = machineQuery.getUserId();
            // 根据状态来查询
            Integer status = machineQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            machineLambdaQueryWrapper
                    .eq(id != null && id > 0, WeldingMachine::getId, id)
                    .in(CollectionUtils.isNotEmpty(idList), WeldingMachine::getId, idList)
                    .and(StringUtils.isNotBlank(searchText),
                            qw -> qw.like(WeldingMachine::getMachineName, searchText)
                                    .or()
                                    .like(WeldingMachine::getNotes, searchText)
                    )
                    .like(StringUtils.isNotBlank(name), WeldingMachine::getMachineName, name)
                    .like(StringUtils.isNotBlank(description), WeldingMachine::getNotes, description)
                    .eq(userId != null && userId > 0, WeldingMachine::getUserId, userId)
                    .eq(WeldingMachine::getMachineStatus, statusEnum.getValue())
                    .orderBy(true, false, WeldingMachine::getCreateTime);
        }
        return listTeamByCondition(currentPage, machineLambdaQueryWrapper);
    }
    /**
     * 按条件列出设备
     *
     * @param currentPage            当前页码
     * @param machineLambdaQueryWrapper 团队lambda查询包装器
     * @return {@link Page}<{@link WeldingMachineVO}>
     */
    public Page<WeldingMachineVO> listTeamByCondition(long currentPage, LambdaQueryWrapper<WeldingMachine> machineLambdaQueryWrapper) {
        Page<WeldingMachine> teamPage = this.page(new Page<>(currentPage, PAGE_SIZE), machineLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(teamPage.getRecords())) {
            return new Page<>();
        }
        Page<WeldingMachineVO> machineVoPage = new Page<>();
        // 关联查询创建人的用户信息
        BeanUtils.copyProperties(teamPage, machineVoPage, "records");
        List<WeldingMachine> teamPageRecords = teamPage.getRecords();
        ArrayList<WeldingMachineVO> teamUserVOList = new ArrayList<>();
        for (WeldingMachine weldingMachine : teamPageRecords) {
            Long userId = weldingMachine.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            WeldingMachineVO machineUserVO = new WeldingMachineVO();
            BeanUtils.copyProperties(weldingMachine, machineUserVO);
            // 脱敏用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                machineUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(machineUserVO);
        }
        machineVoPage.setRecords(teamUserVOList);
        return machineVoPage;
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




