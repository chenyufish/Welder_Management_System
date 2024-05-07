package com.fishman.welder_management_backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fishman.welder_management_backend.common.BaseResponse;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.common.ResultUtils;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.request.*;
import com.fishman.welder_management_backend.model.vo.BlogVO;
import com.fishman.welder_management_backend.model.vo.WeldingMachineFaultsVO;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingMachineFaultsService;
import com.fishman.welder_management_backend.service.WeldingUsageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户控制器
 *
 */
@RestController
@RequestMapping("/faults")
@Slf4j
@Api(tags = "故障报告模块")
public class WeldingMachineFaultsController {

    @Resource
    private UserService userService;
    
    @Resource
    private WeldingMachineFaultsService weldingMachineFaultsService;

    /**
     * 故障列表页面
     *
     * @param currentPage 当前页面
     * @param title 题目
     * @param request     请求
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取故障报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineFaultsVO>> listMachineFaultsPage(long currentPage, String title, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.success(weldingMachineFaultsService.pageFaults(currentPage, title, null));
        } else {
            return ResultUtils.success(weldingMachineFaultsService.pageFaults(currentPage, title, loginUser.getId()));
        }
    }

    /**
     * 添加故障
     *
     * @param faultsAddRequest 故障报告添加请求
     * @param request        请求
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加故障报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "faultsAddRequest", value = "报告添加请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> addFaults(FaultsAddRequest faultsAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (StringUtils.isAnyBlank(faultsAddRequest.getTitle(), faultsAddRequest.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        weldingMachineFaultsService.addFaults(faultsAddRequest, loginUser);
        return ResultUtils.success("添加成功");
    }

    /**
     * 获取我的故障列表
     *
     * @param currentPage 当前页面
     * @param request     请求
     */
    @GetMapping("/list/my/blog")
    @ApiOperation(value = "获取我写的故障报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineFaultsVO>> listMyFaults(long currentPage, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Page<WeldingMachineFaultsVO> faultsPage = weldingMachineFaultsService.listMyFaults(currentPage, loginUser.getId());
        return ResultUtils.success(faultsPage);
    }

    /**
     * 根据id获取故障报告
     *
     * @param id      id
     * @param request 请求
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取故障报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "报告id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<WeldingMachineFaultsVO> getFaultsById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(weldingMachineFaultsService.getFaultsById(id, loginUser.getId()));
    }

    /**
     * 删除故障通过id
     *
     * @param id      id
     * @param request 请求
     */
    // TODO: 权限控制
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "博文id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> deleteBlogById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean admin = userService.isAdmin(loginUser);
        weldingMachineFaultsService.deleteFaults(id, loginUser.getId(), admin);
        return ResultUtils.success("删除成功");
    }
    /**
     * 更新故障报告
     *
     * @param faultsUpdateRequest 故障报告更新请求
     * @param request           请求
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新故障报告")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "faultsUpdateRequest", value = "故障更新请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> updateFaults(FaultsUpdateRequest faultsUpdateRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean admin = userService.isAdmin(loginUser);
        weldingMachineFaultsService.updateFaults(faultsUpdateRequest, loginUser.getId(), admin);
        return ResultUtils.success("更新成功");
    }
}
