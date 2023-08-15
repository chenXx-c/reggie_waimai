package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.reggie_waimai.Dto.SetmealDto;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Category;
import com.czc.reggie_waimai.entity.Setmeal;
import com.czc.reggie_waimai.service.CategoryService;
import com.czc.reggie_waimai.service.SetmealDishService;
import com.czc.reggie_waimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {


        //分页构造器对象

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //根据name用like模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);

        //添加排序条件，根据跟新时间降序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        /*使用BeanUtils类的copyProperties方法来复制两个对象之间的属性值。
        其中，pageInfo和dtoPage都是Page对象，"records"是一个属性名称，
        代表Page对象中的一个属性。

在这个例子中，copyProperties方法将会把pageInfo对象中除了"records"属性之外的所有属性值，
复制到dtoPage对象中对应的属性中。也就是说，这行代码的作用是将pageInfo对象中除了"records"属性之外的所有属性值，
复制到dtoPage对象中对应的属性中。这样做的目的是为了避免将"records"属性中的数据覆盖掉，保留原有的数据。*/
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();  //获取分页对象中的数据


        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝

            BeanUtils.copyProperties(item, setmealDto);


//分类id

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);


            }

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

//删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);


    setmealService.removeWithDish(ids);


        return R.success("删除套餐成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }



}
