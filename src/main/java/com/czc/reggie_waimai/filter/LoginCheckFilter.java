package com.czc.reggie_waimai.filter;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czc.reggie_waimai.common.BaseContext;
import com.czc.reggie_waimai.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的url
        String requestURL=request.getRequestURI();

        log.info("拦截到请求：{}",requestURL);
        //不需要处理的请求路径
        String[]urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"

        };
        //判断本次请求是否需要处理

        boolean check = check(urls, requestURL);
        //如果不需要处理，放行
        if(check){
            log.info("拦截到请求：{}不需要处理",requestURL);
           filterChain.doFilter(request,response);
           return;
        }

        //判断后台员工登录状态
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }


        //判断移动端验证码登录状态
        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        //如果未登录返回未登录结果，通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

//        log.info("拦截到请求:{}", request.getRequestURI());
//        filterChain.doFilter(request, response);

    }
      //检查本次请求是否需要发行
    public boolean check(String[]urls  ,String requestURL) {

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if(match){
                return true;
            }
        }
        return  false;

    }
}
