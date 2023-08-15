package com.czc.reggie_waimai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czc.reggie_waimai.Dto.DishDto;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    public R<String> saveDishWithFlavor(DishDto dishDto);

    R<Page> pageByDish(int page, int pageSize, String name);

    public void updateWithFlavor(DishDto dishDto);
//根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);


}
