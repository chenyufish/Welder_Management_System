package com.fishman.management_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fishman.management_backend.mapper.UserTeamMapper;
import com.fishman.management_backend.model.domain.UserTeam;
import com.fishman.management_backend.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author fishman
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-03-28 19:27:22
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




