package com.fishman.usercenter.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fishman.usercenter.mapper.TagMapper;
import com.fishman.usercenter.model.domain.Tag;
import com.fishman.usercenter.service.TagService;
import org.springframework.stereotype.Service;


@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




