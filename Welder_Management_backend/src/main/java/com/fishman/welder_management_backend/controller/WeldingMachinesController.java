package com.fishman.welder_management_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fishman.welder_management_backend.common.BaseResponse;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.common.ResultUtils;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.domain.UserTeam;
import com.fishman.welder_management_backend.model.domain.WeldingMachine;
import com.fishman.welder_management_backend.model.request.*;
import com.fishman.welder_management_backend.model.vo.WeldingMachineVO;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingMachinesService;
import com.fishman.welder_management_backend.service.WeldingUsageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    private WeldingMachinesService weldingMachinesService;


    @Resource
    private UserService userService;


    @Resource
    private WeldingUsageService weldingUsageService;

    /**
     * 获取设备
     * @param currentPage
     * @param machineQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取设备列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "machineQueryRequest", value = "设备查询请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineVO>> listMachines(long currentPage, MachineQueryRequest machineQueryRequest,
                                                HttpServletRequest request) {
        if (machineQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Page<WeldingMachineVO> machineVoPage = weldingMachinesService.listMachines(currentPage, machineQueryRequest, userService.isAdmin(loginUser));
        return ResultUtils.success(machineVoPage);
    }
    /**
     * 获取已使用设备的用户
     *
     * @param loginUser 登录用户
     * @param machinePage  团队页面
     * @return {@link BaseResponse}<{@link Page}<{@link WeldingMachineVO}>>
     */

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
        Long machineId = weldingMachinesService.addMachine(machine, loginUser);
        return ResultUtils.success(machineId);
    }
    /**
     * 获取用户标签
     *
     * @param request 请求
     */
    @GetMapping("/tags")
    @ApiOperation(value = "获取当前设备标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<String>> getMachineTags(HttpServletRequest request,Long machineId) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<String> machineTags = weldingMachinesService.getMachineTags(machineId);
        return ResultUtils.success(machineTags);
    }

    /**
     * 更新用户标签
     *
     * @param tags    标签
     * @param request 请求
     */
    @PutMapping("/update/tags")
    @ApiOperation(value = "更新设备标签")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tags", value = "标签"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> updateMachineTags(@RequestBody List<String> tags, HttpServletRequest request,Long machineId) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        weldingMachinesService.updateMachineTags(tags,machineId);
        return ResultUtils.success("ok");
    }
    /**
     * 获取我创建的设备名单
     *
     * @param currentPage 当前页面
     * @param machineQuery   团队查询
     * @param request     请求
     * @return {@link BaseResponse}<{@link Page}<{@link WeldingMachineVO}>>
     */
    @GetMapping("/list/my/create")
    @ApiOperation(value = "获取我创建的设备")
    @ApiImplicitParams({@ApiImplicitParam(name = "machineQuery", value = "获取设备请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<WeldingMachineVO>> listMyCreateTeams(long currentPage,
                                                                MachineQueryRequest machineQuery,
                                                        HttpServletRequest request) {
        if (machineQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        machineQuery.setUserId(loginUser.getId());
        Page<WeldingMachineVO> machineVOPage = weldingMachinesService.listMyCreate(currentPage, loginUser.getId());
        return ResultUtils.success(machineVOPage);
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
        Page<WeldingMachineVO> machinePage = weldingMachinesService.listMyMachines(currentPage, loginUser.getId());
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
        return ResultUtils.success(weldingMachinesService.getMachineById(id, loginUser.getId()));
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
        weldingMachinesService.deleteMachine(id, loginUser.getId(), admin);
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
            {@ApiImplicitParam(name = "machineUpdateRequest", value = "设备更新请求"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> updateMachine(MachineUpdateRequest machineUpdateRequest, HttpServletRequest request) {
        if(machineUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean admin = userService.isAdmin(loginUser);
        boolean result = weldingMachinesService.updateMachine(machineUpdateRequest, loginUser.getId(), admin);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }




}
