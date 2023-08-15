package com.czc.reggie_waimai.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.User;
import com.czc.reggie_waimai.service.UserService;
import com.czc.reggie_waimai.utils.SMSUtils;
import com.czc.reggie_waimai.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    ;

    @PostMapping("/sendMsg")
/*@RequestBody是Spring MVC框架中的一个注解，用于将HTTP请求体中的数据绑定到方法参数上。
当客户端向服务器发送POST或PUT请求时，请求体中的数据通常是JSON或XML格式的数据。
使用@RequestBody注解可以让Spring MVC自动将请求体中的数据转换成Java对象，然后将该对象作为方法的参数传递进来。*/
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
/*HttpSession是Java Web应用程序中的一个接口，用于在客户端与服务器之间维护会话状态。
在Web应用程序中，HTTP是一种无状态协议，即每个HTTP请求都是独立的，
服务器无法知道两个请求是否来自同一个客户端。为了解决这个问题，
Web应用程序使用会话来跟踪用户的状态和活动。

HttpSession接口提供了一种在客户端和服务器之间存储和检索数据的方式。
当用户第一次访问Web应用程序时，服务器会创建一个唯一的HttpSession对象，
并将该对象的ID存储在客户端的Cookie中。在随后的请求中，客户端会将该Cookie发送给服务器，
服务器使用该ID来检索相应的HttpSession对象。*/

        log.info("发送短信验证码");


        //获取手机号

        String phone = user.getPhone();

        if (!StringUtils.isEmpty(phone)) {
            //随机生产4位验证码

            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code{}:", code);

            //调用短信api完成发送短信

            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            //需要将生成的验证码保存到Session
            session.setAttribute(phone, code);

            return R.success("发送成功");


        }


        return R.error("发送失败");
    }

//移动端用户登录
    @PostMapping("/login")
/*@RequestBody是Spring MVC框架中的一个注解，用于将HTTP请求体中的数据绑定到方法参数上。
当客户端向服务器发送POST或PUT请求时，请求体中的数据通常是JSON或XML格式的数据。
使用@RequestBody注解可以让Spring MVC自动将请求体中的数据转换成Java对象，然后将该对象作为方法的参数传递进来。*/
    public R<User> login(@RequestBody Map map, HttpSession session) {
/*HttpSession是Java Web应用程序中的一个接口，用于在客户端与服务器之间维护会话状态。
在Web应用程序中，HTTP是一种无状态协议，即每个HTTP请求都是独立的，
服务器无法知道两个请求是否来自同一个客户端。为了解决这个问题，
Web应用程序使用会话来跟踪用户的状态和活动。

HttpSession接口提供了一种在客户端和服务器之间存储和检索数据的方式。
当用户第一次访问Web应用程序时，服务器会创建一个唯一的HttpSession对象，
并将该对象的ID存储在客户端的Cookie中。在随后的请求中，客户端会将该Cookie发送给服务器，
服务器使用该ID来检索相应的HttpSession对象。*/
    log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();


        //获取验证码
        String code = map.get("code").toString();

        //从session中获取保存的验证码

        Object codeInSession = session.getAttribute(phone);

        //验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //对比成功说明登录成功
            //比对成功是否是新用户，是则自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);

            if (user == null) {
                user = new User();

                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);


            }
            session.setAttribute("user", user.getId());
            return R.success(user);

        }


        return R.error("登录失败");
    }


}
