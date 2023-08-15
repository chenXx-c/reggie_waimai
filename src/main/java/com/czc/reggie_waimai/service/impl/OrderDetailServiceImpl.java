package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.entity.OrderDetail;
import com.czc.reggie_waimai.mapper.OrderDetailMapper;
import com.czc.reggie_waimai.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
