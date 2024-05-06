package com.fishman.welder_management_backend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.mapper.BlogLikeMapper;
import com.fishman.welder_management_backend.model.domain.BlogLike;
import com.fishman.welder_management_backend.service.BlogLikeService;
import org.springframework.stereotype.Service;

/**
 * @author fishman
 * @description 针对表【blog_like】的数据库操作Service实现
 * @createDate 2023-06-05 21:54:55
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike>
        implements BlogLikeService {

}




