package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Category;
import com.czc.reggie_waimai.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.List;

@RestController

@Slf4j
@RequestMapping("/category")
public class CategroyController {

    @Autowired
    private CategoryService categoryService;

    //新增菜单
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);

        return R.success("新增菜单成功");
    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();


        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);


    }

    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.removeById(id);
        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");

    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){


        LambdaQueryWrapper<Category>queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category>list=categoryService.list(queryWrapper);

        return R.success(list);

    }




}

















