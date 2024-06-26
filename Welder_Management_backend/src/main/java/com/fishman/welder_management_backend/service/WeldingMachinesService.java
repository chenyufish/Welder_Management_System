package com.fishman.welder_management_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.WeldingMachine;
import com.fishman.welder_management_backend.model.request.MachineAddRequest;
import com.fishman.welder_management_backend.model.request.MachineQueryRequest;
import com.fishman.welder_management_backend.model.request.MachineUpdateRequest;
import com.fishman.welder_management_backend.model.request.TeamQueryRequest;
import com.fishman.welder_management_backend.model.vo.BlogVO;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;

import java.util.List;


/**
* @author fishman
* @description 针对表【weldingmachines】的数据库操作Service
* @createDate 2024-05-06 19:01:09
*/
public interface WeldingMachinesService extends IService<WeldingMachine> {
    /**
     * 添加设备
     *
     * @param weldingMachine 设备添加请求
     * @param loginUser      登录用户
     * @return {@link Long}
     */
    long addMachine(WeldingMachine weldingMachine, User loginUser);


    Page<WeldingMachineVO> listMachines(long currentPage, MachineQueryRequest machineQuery, boolean isAdmin);

    /**
     * 列出我使用的设备
     *
     * @param currentPage 当前页码
     * @param id          id
     * @return {@link Page}<{@link WeldingMachineVO}>
     */
    Page<WeldingMachineVO> listMyMachines(long currentPage, Long id);

    /**
     * 收到设备通过id
     *
     * @param machineId 设备id
     * @param userId 用户id
     * @return {@link WeldingMachineVO}
     */
    WeldingMachineVO getMachineById(long machineId, Long userId);
    /**
     * 删除设备
     *
     * @param MachineId  设备id
     * @param userId  用户id
     * @param isAdmin 是否为管理员
     */
    void deleteMachine(Long MachineId, Long userId, boolean isAdmin);

    /**
     * 更新设备
     *
     * @param machineUpdateRequest 设备更新请求
     * @param userId            用户id
     * @param isAdmin           是否为管理员
     */
    boolean updateMachine(MachineUpdateRequest machineUpdateRequest, Long userId, boolean isAdmin);
    /**
     * 收到用户标签
     *
     * @param machineId id
     */
    List<String> getMachineTags(Long machineId);

    /**
     * 更新标记
     *
     * @param tags   标签
     * @param machineId 用户id
     */
    void updateMachineTags(List<String> tags, Long machineId);

    /**
     * 获取我添加到设备
     * @param currentPage
     * @param userId
     * @return
     */
    Page<WeldingMachineVO> listMyCreate(long currentPage, Long userId);

}
