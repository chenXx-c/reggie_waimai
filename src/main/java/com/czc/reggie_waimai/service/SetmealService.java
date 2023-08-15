package com.czc.reggie_waimai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.czc.reggie_waimai.Dto.SetmealDto;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void pageBySetmeal(int page, int pageSize, String name);

    public void sellStatus(Integer status, List<Long> ids);

    public void removeWithDish(List<Long> ids);
}
