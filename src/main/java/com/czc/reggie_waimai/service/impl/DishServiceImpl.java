package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.Dto.DishDto;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Dish;
import com.czc.reggie_waimai.entity.DishFlavor;
import com.czc.reggie_waimai.mapper.DishMapper;
import com.czc.reggie_waimai.service.DishFlavorService;
import com.czc.reggie_waimai.service.DishService;
import com.sun.corba.se.impl.ior.JIDLObjectKeyTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    /*新增菜品，同时保存对应的口味数据*/
    @Override
    public R<String> saveDishWithFlavor(DishDto dishDto) {

        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());


        //保存菜品的口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());


        return R.success("新增菜品成功！");
    }

    @Override
    public R<Page> pageByDish(int page, int pageSize, String name) {
        return null;
    }

    @Override
    @Transactional
    /*`@Transactional` 是 Spring 框架中的一个注解，用于标记一个方法需要进行事务管理。
    事务管理是指将一系列数据库操作作为一个整体来进行管理，保证这些操作要么全部成功，
    要么全部失败回滚。

使用 `@Transactional` 注解可以让 Spring 自动为被注解的方法开启事务，
并在方法结束时自动提交或回滚事务。如果方法执行过程中抛出了一个未捕获的异常，事务会自动回滚，
以保证数据的一致性和完整性。

`@Transactional` 注解可以标记在类或方法上，如果标记在类上，
则表示该类中所有的方法都需要进行事务管理。如果标记在方法上，则表示该方法需要进行事务管理。
在方法上标记的 `@Transactional` 注解会覆盖类上的注解。

`@Transactional` 注解还可以接受一些参数，用于控制事务的行为，
例如事务的隔离级别、事务的传播行为、事务的超时时间等。这些参数可以根据具体的业务需求进行配置。*/

    public void updateWithFlavor(DishDto dishDto) {

        //更新dish基本信息
        this.updateById(dishDto);


        //清理当前菜品对应口味数据--dish_flavor的delete操作

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);


        //添加新的菜品口味数据到dish_flavor
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }


    @Override
    public DishDto getByIdWithFlavor(Long id) {


        //查询当前菜单基本信息，从dish表中查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品的口味信息，从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);


        dishDto.setFlavors(flavors);

        return dishDto;
    }



}
