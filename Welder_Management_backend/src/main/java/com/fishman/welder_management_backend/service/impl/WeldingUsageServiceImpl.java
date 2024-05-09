package com.fishman.welder_management_backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.mapper.WeldingUsageMapper;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.WeldingMachine;
import com.fishman.welder_management_backend.model.domain.WeldingUsage;
import com.fishman.welder_management_backend.model.request.MachineBorrowRequest;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;
import com.fishman.welder_management_backend.model.vo.WeldingUsageVO;
import com.fishman.welder_management_backend.service.WeldingMachinesService;
import com.fishman.welder_management_backend.service.WeldingUsageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author fishman
* @description 针对表【weldingusage】的数据库操作Service实现
* @createDate 2024-05-06 19:01:12
*/
@Service
public class WeldingUsageServiceImpl extends ServiceImpl<WeldingUsageMapper, WeldingUsage>
    implements WeldingUsageService {

    @Resource
    WeldingMachinesService weldingMachinesService;
    @Override
    public Long borrowMachine(MachineBorrowRequest machineBorrowRequest, User loginUser) {
        String machineId = String.valueOf(machineBorrowRequest.getMachineId());
        String employeeId = String.valueOf(machineBorrowRequest.getEmployeeId());
        WeldingUsageVO weldingUsageVO = new WeldingUsageVO();
        Integer status =weldingUsageVO.getMachineStatus();
        if (status == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (employeeId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingUsage weldingUsage = new WeldingUsage();
        weldingUsage.setMachineID(Long.valueOf(machineId));
        weldingUsage.setEmployeeID(Long.valueOf(employeeId));
        WeldingMachine weldingMachine=new WeldingMachine();
        weldingMachine.setMachineStatus(1);
        boolean result = this.save(weldingUsage);
        if (result) {
            return weldingUsage.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public Long returnMachine(MachineBorrowRequest machineBorrowRequest, User loginUser) {
        String machineId = String.valueOf(machineBorrowRequest.getMachineId());
        String employeeId = String.valueOf(machineBorrowRequest.getEmployeeId());
        WeldingMachineVO weldingMachineVO = weldingMachinesService.getMachineById(Long.parseLong(machineId), loginUser.getId());
        weldingMachineVO.getMachineStatus();
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (employeeId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        WeldingUsage weldingUsage = new WeldingUsage();
        weldingUsage.setMachineID(Long.valueOf(machineId));
        weldingUsage.setEmployeeID(Long.valueOf(employeeId));
        WeldingMachine weldingMachine=new WeldingMachine();
        weldingMachine.setMachineStatus(0);
        boolean result = this.save(weldingUsage);
        if (result) {
            return weldingUsage.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

}




