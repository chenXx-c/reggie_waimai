package com.czc.reggie_waimai.Dto;

import com.czc.reggie_waimai.entity.Setmeal;
import com.czc.reggie_waimai.entity.SetmealDish;

import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
