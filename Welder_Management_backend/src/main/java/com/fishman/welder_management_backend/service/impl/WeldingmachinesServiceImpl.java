package com.fishman.welder_management_backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.mapper.WeldingmachinesMapper;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.Weldingmachines;
import com.fishman.welder_management_backend.model.request.MachineAddRequest;
import com.fishman.welder_management_backend.model.request.MachineUpdateRequest;
import com.fishman.welder_management_backend.model.vo.MachineVO;
import com.fishman.welder_management_backend.service.WeldingmachinesService;
import org.springframework.stereotype.Service;

/**
* @author fishman
* @description 针对表【weldingmachines】的数据库操作Service实现
* @createDate 2024-05-06 19:01:09
*/
@Service
public class WeldingmachinesServiceImpl extends ServiceImpl<WeldingmachinesMapper, Weldingmachines>
    implements WeldingmachinesService {
    @Override
    public Long addMachine(MachineAddRequest machineAddRequest, User loginUser) {
        return null;
    }

    @Override
    public Page<MachineVO> listMyMachines(long currentPage, Long id) {
        return null;
    }

    @Override
    public Page<MachineVO> pageMachine(long currentPage, String machineName, Long id) {
        return null;
    }

    @Override
    public MachineVO getMachineById(long machineId, Long userId) {
        return null;
    }

    @Override
    public void deleteMachine(Long MachineId, Long userId, boolean isAdmin) {

    }

    @Override
    public void updateMachine(MachineUpdateRequest machineUpdateRequest, Long userId, boolean isAdmin) {

    }

}




