package com.czc.reggie_waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czc.reggie_waimai.entity.Employee;
import com.czc.reggie_waimai.mapper.EmployeeMapper;
import com.czc.reggie_waimai.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
