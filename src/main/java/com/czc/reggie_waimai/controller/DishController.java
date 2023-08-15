package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.reggie_waimai.Dto.DishDto;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Category;
import com.czc.reggie_waimai.entity.Dish;
import com.czc.reggie_waimai.entity.DishFlavor;
import com.czc.reggie_waimai.mapper.CategoryMapper;
import com.czc.reggie_waimai.service.CategoryService;
import com.czc.reggie_waimai.service.DishFlavorService;
import com.czc.reggie_waimai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {//@RequestBody转json
        log.info(dishDto.toString());
        dishService.saveDishWithFlavor(dishDto);

        return R.success("新增菜品成功");

    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页构造对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        Page<DishDto> dishDtoPage = new Page<>();


        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询

        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
    /*这段代码的作用是将 dishDtoPage 对象中除了 records 属性以外的所有属性的值复制到
     pageInfo 对象中对应的属性中。这个操作是通过使用 BeanUtils.copyProperties 方法
     实现的。这个方法是 Spring 框架中的一个工具类方法，可以将一个 Java 对象的属性值复制到另一个 Java 对象
     的对应属性中*/
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //PageInfo 对象的 getRecords() 方法返回属于当前页的记录列表
        List<Dish> records = pageInfo.getRecords();
        /* 定义一个DishDto类型的列表collect，然后将记录列表records转换为一个Stream流，对每个记录进行映射。*/
        List<DishDto> collect = records.stream().map((item) -> {

            DishDto dishDto = new DishDto();
            /* 将记录(item)的属性值复制到DishDto对象(dishDto)中。*/
            BeanUtils.copyProperties(item, dishDto);
            /*获取记录的categoryId属性值。*/
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            /*如果查询到了Category对象。*/
            if (category != null) {
                /* 获取Category对象的名称属性值。*/
                String categoryName = category.getName();
                /*将Category对象的名称属性值设置到DishDto对象的categoryName属性中。*/
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());// 将Stream流转换为List列表(collect)。

        dishDtoPage.setRecords(collect);
        return R.success(pageInfo);

    }

    //根据id查询菜品信息和对应的口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {//@RequestBody转json
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");

    }

    //根据条件查询对应菜品数据
   /* @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish>queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());


        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getSort);

        List<Dish>list=dishService.list(queryWrapper);




        return R.success(list);
    }*/


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件，查询状态为1的（起售状态）
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //条件排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(lambdaQueryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL: select* from dishflavor where dish_id=?;
            List<DishFlavor> dishFlavorlist = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorlist);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


}
