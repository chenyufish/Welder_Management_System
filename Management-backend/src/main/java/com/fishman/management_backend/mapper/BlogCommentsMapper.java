package com.fishman.management_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fishman.management_backend.model.domain.BlogComments;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fishman
* @description 针对表【blog_comments】的数据库操作Mapper
*
* 
*/
@Mapper
public interface BlogCommentsMapper extends BaseMapper<BlogComments> {

}




