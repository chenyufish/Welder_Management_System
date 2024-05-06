package com.fishman.management_backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fishman.management_backend.model.domain.Message;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




