package com.czc.reggie_waimai.Dto;

import com.czc.reggie_waimai.entity.Dish;
import com.czc.reggie_waimai.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
