package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.entity.ShoppingCart;
import com.czc.reggie_waimai.mapper.ShoppingCartMapper;
import com.czc.reggie_waimai.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {
}
