package com.fishman.usercenter.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fishman.usercenter.model.domain.Team;
import com.fishman.usercenter.model.domain.User;
import com.fishman.usercenter.model.request.*;
import com.fishman.usercenter.model.vo.TeamUserVO;
import com.fishman.usercenter.model.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 团队服务
 *
 */
public interface TeamService extends IService<Team> {

    /**
     * 添加团队
     *
     * @param team      团队
     * @param loginUser 登录用户
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    long addTeam(Team team, User loginUser);

    /**
     * 列出团队
     *
     * @param currentPage 当前页码
     * @param teamQuery   团队查询
     * @param isAdmin     是否为管理员
     */
    Page<TeamUserVO> listTeams(long currentPage, TeamQueryRequest teamQuery, boolean isAdmin);

    /**
     * 更新团队
     *
     * @param teamUpdateRequest 团队更新请求
     * @param loginUser         登录用户
     * @return boolean
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入团队
     *
     * @param teamJoinRequest 团队加入请求
     * @param loginUser       登录用户
     * @return boolean
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出团队
     *
     * @param teamQuitRequest 团队退出请求
     * @param loginUser       登录用户
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除团队
     *
     * @param id        id
     * @param loginUser 登录用户
     * @param isAdmin   是否为管理员
     * @return boolean
     */
    boolean deleteTeam(long id, User loginUser, boolean isAdmin);

    /**
     * 获得团队
     *
     * @param teamId 团队id
     * @param userId 用户id
     * @return {@link TeamUserVO}
     */
    TeamUserVO getTeam(Long teamId, Long userId);

    /**
     * 列出我加入
     *
     * @param currentPage 当前页码
     * @param teamQuery   团队查询
     */
    Page<TeamUserVO> listMyJoin(long currentPage, TeamQueryRequest teamQuery);

    /**
     * 获取团队成员
     *
     * @param teamId 团队id
     * @param userId 用户id
     */
    List<UserVO> getTeamMember(Long teamId, Long userId);

    /**
     * 列出我所有加入
     *
     * @param id id
     */
    List<TeamUserVO> listAllMyJoin(Long id);

    /**
     * 更改封面图像
     *
     * @param request 要求
     * @param userId  用户id
     * @param admin   管理
     */
    void changeCoverImage(TeamCoverUpdateRequest request, Long userId, boolean admin);

    /**
     * 踢出
     *
     * @param teamId      团队id
     * @param userId      用户id
     * @param loginUserId 登录用户id
     * @param admin       管理
     */
    void kickOut(Long teamId, Long userId, Long loginUserId, boolean admin);

    /**
     * 列出我创建
     *
     * @param currentPage 当前页码
     * @param userId      用户id
     */
    Page<TeamUserVO> listMyCreate(long currentPage, Long userId);

    /**
     * 获取已加入队员头像
     *
     * @param TeamUserVOPage 团队vo分页
     */
    Page<TeamUserVO> getJoinedUserAvatar(Page<TeamUserVO> TeamUserVOPage);

}
