package com.czc.reggie_waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.czc.reggie_waimai.entity.Orders;
import com.czc.reggie_waimai.mapper.OrderMapper;

public interface OrderService extends IService<Orders> {

    public void submit(Orders order);


}
