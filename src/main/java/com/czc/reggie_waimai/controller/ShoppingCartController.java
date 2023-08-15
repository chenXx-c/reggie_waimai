package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.reggie_waimai.common.BaseContext;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.ShoppingCart;
import com.czc.reggie_waimai.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

//查看购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>>list(){
        log.info("查看购物车");

        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart>list=shoppingCartService.list(queryWrapper);
        return R.success(list);


    }
//新增购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据{}：",shoppingCart);

    //设置用户的id,指明当前是那个id
        Long currentId= BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜单后者套餐是否已经在购物车中
        Long dishId=shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId,currentId);

        if(dishId!=null){
            //添加到购物车的位菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else{
            //添加到购物车的位套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        }

        ShoppingCart cartServiceone=shoppingCartService.getOne(queryWrapper);

        if(cartServiceone!=null){
            //如果已经存在，在原来基础上加一
            Integer number=cartServiceone.getNumber();

            cartServiceone.setNumber(number+1);

            shoppingCartService.updateById(cartServiceone);

        }else{
            //如果不存在，后卫车默认位一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartService.save(shoppingCart);
            cartServiceone=shoppingCart;

        }





        return R.success(cartServiceone);

    }

    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart>queryWrapper=new LambdaQueryWrapper();

        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");


    }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        if (setmealId!=null){
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        Integer number = one.getNumber();
        if(number==1){
            shoppingCartService.remove(queryWrapper);
        }else {
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }






}
