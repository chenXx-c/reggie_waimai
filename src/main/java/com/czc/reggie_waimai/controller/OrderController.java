package com.czc.reggie_waimai.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.reggie_waimai.common.R;

import com.czc.reggie_waimai.entity.Orders;
import com.czc.reggie_waimai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

//用户下单
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order) {

        log.info("订单数据：{}",order);
        orderService.submit(order);
        return R.success("下单成功");


    }

}
