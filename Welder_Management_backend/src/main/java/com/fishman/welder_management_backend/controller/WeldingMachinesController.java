package com.fishman.welder_management_backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fishman.welder_management_backend.common.BaseResponse;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.common.ResultUtils;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.WeldingMachine;
import com.fishman.welder_management_backend.model.request.*;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingMachineFaultsService;
import com.fishman.welder_management_backend.service.WeldingMachinesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 设备控制器
 *
 */
//TODO 整个页面都需要鉴权
@RestController
@RequestMapping("/machine")
@Slf4j
@Api(tags = "设备管理模块")
public class WeldingMachinesController {

    /**
     * 设备服务
     */
    @Resource
    private WeldingMachinesService weldingmachinesService;


    @Resource
    private UserService userService;


    /**
     * 获取设备
     * @param currentPage
     * @param machineName
     * @param request
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取设备列表")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineVO>> listMachinesPage(long currentPage, String machineName, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            return ResultUtils.success(weldingmachinesService.pageWeldingMachine(currentPage, machineName, null));
        } else {
            return ResultUtils.success(weldingmachinesService.pageWeldingMachine(currentPage, machineName, loginUser.getId()));
        }
    }
    /**
     * 添加设备
     *
     * @param machineAddRequest 设备添加请求
     * @param request        请求
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "MachineAddRequest", value = "添加设备参数"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Long> addMachine(@RequestBody MachineAddRequest machineAddRequest, HttpServletRequest request) {
        if (machineAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        WeldingMachine machine = new WeldingMachine();
        BeanUtils.copyProperties(machineAddRequest, machine);
        Long machineId = weldingmachinesService.addMachine(machine, loginUser);
        return ResultUtils.success(machineId);
    }
    /**
     * 我使用的列表
     *
     * @param currentPage 当前页面
     * @param request     请求
     */
    @GetMapping("/list/my/used")
    @ApiOperation(value = "获取我使用过的设备列表")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "currentPage", value = "当前页"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineVO>> listMyBlogs(long currentPage, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Page<WeldingMachineVO> machinePage = weldingmachinesService.listMyMachines(currentPage, loginUser.getId());
        return ResultUtils.success(machinePage);
    }
    /**
     * 通过id获取设备
     *
     * @param id      id
     * @param request 请求
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "设备id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<WeldingMachineVO> getBlogById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(weldingmachinesService.getMachineById(id, loginUser.getId()));
    }
    /**
     * 删除设备通过id
     *
     * @param id      id
     * @param request 请求
     */
    //todo 鉴权
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "设备id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> deleteMachineById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean admin = userService.isAdmin(loginUser);
        weldingmachinesService.deleteMachine(id, loginUser.getId(), admin);
        return ResultUtils.success("删除成功");
    }
    /**
     * 更新博设备
     *
     * @param machineUpdateRequest
     * uest 设备更新请求
     * @param request           请求
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "blogUpdateRequest", value = "设备更新请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> updateMachine(MachineUpdateRequest machineUpdateRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean admin = userService.isAdmin(loginUser);
        weldingmachinesService.updateMachine(machineUpdateRequest, loginUser.getId(), admin);
        return ResultUtils.success("更新成功");
    }




}
