package com.yupi.usercenter.mapper;


import com.yupi.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 用户映射器
 *
 * @description 针对表【user】的数据库操作Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 收到随机用户
     *
     */
    List<User> getRandomUser();
}




