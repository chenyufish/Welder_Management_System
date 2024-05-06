package com.fishman.usercenter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fishman.usercenter.model.domain.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fishman
* @description 针对表【follow】的数据库操作Mapper
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




