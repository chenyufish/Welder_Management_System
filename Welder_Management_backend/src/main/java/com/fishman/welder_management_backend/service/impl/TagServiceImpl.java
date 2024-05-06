package com.fishman.welder_management_backend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fishman.welder_management_backend.mapper.TagMapper;
import com.fishman.welder_management_backend.model.domain.Tag;
import com.fishman.welder_management_backend.service.TagService;
import org.springframework.stereotype.Service;


@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




