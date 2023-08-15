package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.entity.Category;
import com.czc.reggie_waimai.mapper.CategoryMapper;
import com.czc.reggie_waimai.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>implements CategoryService {
}
