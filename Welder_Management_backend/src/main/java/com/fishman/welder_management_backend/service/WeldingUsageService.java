package com.fishman.welder_management_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.WeldingUsage;
import com.fishman.welder_management_backend.model.request.MachineBorrowRequest;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;


/**
* @author fishman
* @description 针对表【weldingusage】的数据库操作Service
* @createDate 2024-05-06 19:01:12
*/
public interface WeldingUsageService extends IService<WeldingUsage> {

    Long borrowMachine(MachineBorrowRequest machineBorrowRequest, User loginUser);

    Long returnMachine(MachineBorrowRequest machineBorrowRequest, User loginUser);
}
