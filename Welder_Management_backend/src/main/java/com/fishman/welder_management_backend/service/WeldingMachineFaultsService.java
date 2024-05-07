package com.fishman.welder_management_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.WeldingMachineFaults;
import com.fishman.welder_management_backend.model.request.FaultsAddRequest;
import com.fishman.welder_management_backend.model.request.FaultsUpdateRequest;
import com.fishman.welder_management_backend.model.vo.BlogVO;
import com.fishman.welder_management_backend.model.vo.WeldingMachineFaultsVO;


/**
* @author fishman
* @description 针对表【weldingmachinefaults】的数据库操作Service
* @createDate 2024-05-06 19:01:07
*/
public interface WeldingMachineFaultsService extends IService<WeldingMachineFaults> {
    /**
     * 添加故障报告
     *
     * @param faultsAddRequest 故障报告添加请求
     * @param loginUser      登录用户
     * @return {@link Long}
     */
    Long addFaults(FaultsAddRequest faultsAddRequest, User loginUser);

    /**
     * 列出我故障报告
     *
     * @param currentPage 当前页码
     * @param id          id
     * @return {@link Page}<{@link BlogVO}>
     */
    Page<WeldingMachineFaultsVO> listMyFaults(long currentPage, Long id);


    /**
     * 分页故障报告
     *
     * @param currentPage 当前页码
     * @param title       标题
     * @param id          id
     * @return {@link Page}<{@link BlogVO}>
     */
    Page<WeldingMachineFaultsVO> pageFaults(long currentPage, String title, Long id);

    /**
     * 收到故障报告通过id
     *
     * @param blogId 故障报告id
     * @param userId 用户id
     * @return {@link BlogVO}
     */
    WeldingMachineFaultsVO getFaultsById(long blogId, Long userId);

    /**
     * 删除故障报告
     *
     * @param blogId  故障报告id
     * @param userId  用户id
     * @param isAdmin 是否为管理员
     */
    void deleteFaults(Long blogId, Long userId, boolean isAdmin);

    /**
     * 更新故障报告
     *
     * @param faultsupdateRequest 故障报告更新请求
     * @param userId            用户id
     * @param isAdmin           是否为管理员
     */
    void updateFaults(FaultsUpdateRequest faultsupdateRequest, Long userId, boolean isAdmin);

}
