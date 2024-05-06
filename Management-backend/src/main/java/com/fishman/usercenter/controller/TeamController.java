package com.fishman.usercenter.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fishman.usercenter.common.BaseResponse;
import com.fishman.usercenter.common.DeleteRequest;
import com.fishman.usercenter.common.ErrorCode;
import com.fishman.usercenter.common.ResultUtils;
import com.fishman.usercenter.exception.BusinessException;
import com.fishman.usercenter.model.domain.Team;
import com.fishman.usercenter.model.domain.User;
import com.fishman.usercenter.model.domain.UserTeam;
import com.fishman.usercenter.model.request.*;
import com.fishman.usercenter.model.vo.TeamUserVO;
import com.fishman.usercenter.model.vo.UserVO;
import com.fishman.usercenter.service.TeamService;
import com.fishman.usercenter.service.UserService;
import com.fishman.usercenter.service.UserTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍控制器
 *
 */
@RestController
@RequestMapping("/team")
@Api(tags = "队伍管理模块")
@Log4j2
public class TeamController {
    /**
     * 团队服务
     */
    @Resource
    private TeamService teamService;

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 用户团队服务
     */
    @Resource
    private UserTeamService userTeamService;

    /**
     * 加入团队
     *
     * @param teamAddRequest 团队添加请求
     * @param request        请求
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加队伍")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "teamAddRequest", value = "队伍添加请求参数"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 更新团队
     *
     * @param teamUpdateRequest 团队更新请求
     * @param request           请求
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamUpdateRequest", value = "队伍更新请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,
                                            HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }


    /**
     * 通过id获取团队
     *
     * @param id      id
     * @param request 请求
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "队伍id")})
    public BaseResponse<TeamUserVO> getTeamById(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(teamService.getTeam(id, loginUser.getId()));
    }

    /**
     * 团队名单
     *
     * @param currentPage      当前页面
     * @param teamQueryRequest 团队查询请求
     * @param request          请求
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取队伍列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQueryRequest", value = "队伍查询请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamUserVO>> listTeams(long currentPage, TeamQueryRequest teamQueryRequest,
                                                HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Page<TeamUserVO> TeamUserVOPage = teamService.listTeams(currentPage, teamQueryRequest, userService.isAdmin(loginUser));
        Page<TeamUserVO> TeamUserVOPageWithAvatar = teamService.getJoinedUserAvatar(TeamUserVOPage);
        Page<TeamUserVO> finalPage = getTeamHasJoinNum(TeamUserVOPageWithAvatar);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 加入团队
     *
     * @param teamJoinRequest 团队加入请求
     * @param request         请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/join")
    @ApiOperation(value = "加入队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamJoinRequest", value = "加入队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 退出团队
     *
     * @param teamQuitRequest 团队辞职请求
     * @param request         请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/quit")
    @ApiOperation(value = "退出队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuitRequest", value = "退出队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 删除团队
     *
     * @param deleteRequest 删除请求
     * @param request       请求
     */
    @PostMapping("/delete")
    @ApiOperation(value = "解散队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "deleteRequest", value = "解散队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean isAdmin = userService.isAdmin(loginUser);
        boolean result = teamService.deleteTeam(id, loginUser, isAdmin);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 我创建团队名单
     *
     * @param currentPage 当前页面
     * @param teamQuery   团队查询
     * @param request     请求
     */
    @GetMapping("/list/my/create")
    @ApiOperation(value = "获取我创建的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamUserVO>> listMyCreateTeams(long currentPage,
                                                        TeamQueryRequest teamQuery,
                                                        HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        teamQuery.setUserId(loginUser.getId());
        Page<TeamUserVO> TeamUserVOPage = teamService.listMyCreate(currentPage, loginUser.getId());
        Page<TeamUserVO> finalPage = getTeamHasJoinNum(TeamUserVOPage);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 我加入团队名单
     *
     * @param currentPage 当前页面
     * @param teamQuery   团队查询
     * @param request     请求
     * @return {@link BaseResponse}<{@link Page}<{@link TeamUserVO}>>
     */
    @GetMapping("/list/my/join")
    @ApiOperation(value = "获取我加入的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamUserVO>> listMyJoinTeams(long currentPage,
                                                      TeamQueryRequest teamQuery,
                                                      HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(userTeamLambdaQueryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        if (idList.isEmpty()) {
            return ResultUtils.success(new Page<>());
        }
        teamQuery.setIdList(idList);
        Page<TeamUserVO> TeamUserVOPage = teamService.listMyJoin(currentPage, teamQuery);
        Page<TeamUserVO> finalPage = getTeamHasJoinNum(TeamUserVOPage);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 列出所有我加入团队
     *
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link TeamUserVO}>>
     */
    @GetMapping("/list/my/join/all")
    @ApiOperation(value = "获取我加入的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<TeamUserVO>> listAllMyJoinTeams(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<TeamUserVO> TeamUserVOList = teamService.listAllMyJoin(loginUser.getId());
        return ResultUtils.success(TeamUserVOList);
    }

    /**
     * 通过id获取团队成员
     *
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("/member/{id}")
    @ApiOperation(value = "获取队伍成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "队伍id"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<UserVO>> getTeamMemberById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserVO> teamMember = teamService.getTeamMember(id, loginUser.getId());
        return ResultUtils.success(teamMember);
    }

    /**
     * 更新封面图片
     *
     * @param teamCoverUpdateRequest 团队包括变更请求
     * @param request                请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PutMapping("/cover")
    @ApiOperation(value = "更新封面图片")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamCoverUpdateRequest", value = "队伍封面更新请求"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> changeCoverImage(TeamCoverUpdateRequest teamCoverUpdateRequest,
                                                 HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean admin = userService.isAdmin(loginUser);
        teamService.changeCoverImage(teamCoverUpdateRequest, loginUser.getId(), admin);
        return ResultUtils.success("ok");
    }

    /**
     * 踢出队员
     *
     * @param teamKickOutRequest 踢出队员请求
     * @param request            请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/kick")
    @ApiOperation(value = "踢出队员")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamKickOutRequest", value = "踢出队员请求"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> kickOut(@RequestBody TeamKickOutRequest teamKickOutRequest,
                                        HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long teamId = teamKickOutRequest.getTeamId();
        Long userId = teamKickOutRequest.getUserId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean admin = userService.isAdmin(loginUser);
        teamService.kickOut(teamId, userId, loginUser.getId(), admin);
        return ResultUtils.success("ok");
    }

    /**
     * 获取已加入的用户
     *
     * @param loginUser 登录用户
     * @param teamPage  团队页面
     */
    private BaseResponse<Page<TeamUserVO>> getUserJoinedList(User loginUser, Page<TeamUserVO> teamPage) {
        try {
            List<TeamUserVO> teamList = teamPage.getRecords();
            List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
            //判断当前用户已加入的队伍
            LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, loginUser.getId()).in(UserTeam::getTeamId, teamIdList);
            //用户已加入的队伍
            List<UserTeam> userTeamList = userTeamService.list(userTeamLambdaQueryWrapper);
            Set<Long> joinedTeamIdList = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team -> team.setHasJoin(joinedTeamIdList.contains(team.getId())));
            teamPage.setRecords(teamList);
        } catch (Exception ignored) {
        }
        return ResultUtils.success(teamPage);
    }

    /**
     * 获取队伍已加入人数
     *
     * @param TeamUserVOPage TeamUserVOPage
     */
    private Page<TeamUserVO> getTeamHasJoinNum(Page<TeamUserVO> TeamUserVOPage) {
        List<TeamUserVO> teamList = TeamUserVOPage.getRecords();
        teamList.forEach((team) -> {
            LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, team.getId());
            long hasJoinNum = userTeamService.count(userTeamLambdaQueryWrapper);
            team.setHasJoinNum(hasJoinNum);
        });
        TeamUserVOPage.setRecords(teamList);
        return TeamUserVOPage;
    }
}
