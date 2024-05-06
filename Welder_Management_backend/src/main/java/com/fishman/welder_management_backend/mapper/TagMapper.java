package com.fishman.welder_management_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.fishman.welder_management_backend.model.domain.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * 标签映射器
 *
 * @author fishman23/07/28
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




