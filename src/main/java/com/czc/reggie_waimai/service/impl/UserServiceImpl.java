package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.entity.User;
import com.czc.reggie_waimai.mapper.UserMapper;
import com.czc.reggie_waimai.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

}
