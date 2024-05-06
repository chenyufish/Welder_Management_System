package com.fishman.welder_management_backend.mapper;

import com.fishman.welder_management_backend.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author fishman
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-05-06 18:27:08
* @Entity com.fishman.welder_management_backend.model.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<User> getRandomUser();
}




