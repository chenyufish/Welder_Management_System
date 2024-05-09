package com.fishman.welder_management_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fishman.welder_management_backend.common.BaseResponse;
import com.fishman.welder_management_backend.common.ErrorCode;
import com.fishman.welder_management_backend.common.ResultUtils;
import com.fishman.welder_management_backend.exception.BusinessException;
import com.fishman.welder_management_backend.model.domain.User;
import com.fishman.welder_management_backend.model.request.*;
import com.fishman.welder_management_backend.model.vo.UserVO;
import com.fishman.welder_management_backend.service.UserService;
import com.fishman.welder_management_backend.service.WeldingUsageService;
import com.fishman.welder_management_backend.utils.MessageUtils;
import com.fishman.welder_management_backend.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fishman.welder_management_backend.contant.RedisConstants.*;
import static com.fishman.welder_management_backend.contant.SystemConstants.PAGE_SIZE;
import static com.fishman.welder_management_backend.contant.UserConstants.ADMIN_ROLE;


/**
 * 用户控制器
 *
 */
@RestController
@RequestMapping("/usage")
@Slf4j
@Api(tags = "设备借用模块")
public class WeldingUsageController {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    @Resource
    private WeldingUsageService weldingUsageService;

    @Value("${spring.mail.username}")
    private String userFrom;
    /**
     * 用户注册
     *
     * @param machineBorrowRequest 用户注册请求
     * @param request             要求
     */
    @PostMapping("/borrow")
    @ApiOperation(value = "借用设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "MachineBorrowRequest", value = "借用设备请求参数")})
    public BaseResponse<String>machineBorrow(@RequestBody MachineBorrowRequest machineBorrowRequest,
                                             HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);

        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (machineBorrowRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String machineId = String.valueOf(machineBorrowRequest.getMachineId());
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String employeeId = String.valueOf(machineBorrowRequest.getEmployeeId());
        if (employeeId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        weldingUsageService.borrowMachine(machineBorrowRequest, loginUser);
        return ResultUtils.success("借用成功");

    }
    @PostMapping("/return")
    @ApiOperation(value = "归还设备")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "MachineBorrowRequest", value = "设备归还请求参数")})
    public BaseResponse<String>machineReturn(@RequestBody MachineBorrowRequest machineBorrowRequest,
                                             HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);

        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (machineBorrowRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String machineId = String.valueOf(machineBorrowRequest.getMachineId());
        if (machineId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String employeeId = String.valueOf(machineBorrowRequest.getEmployeeId());
        if (employeeId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        weldingUsageService.returnMachine(machineBorrowRequest, loginUser);
        return ResultUtils.success("归还成功");
    }

}
