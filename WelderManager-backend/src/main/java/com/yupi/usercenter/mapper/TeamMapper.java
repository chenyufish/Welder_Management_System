package com.yupi.usercenter.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.usercenter.model.domain.Team;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description 针对表【team(队伍)】的数据库操作Mapper
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

}




