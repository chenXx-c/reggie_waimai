package com.czc.reggie_waimai.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*`@ControllerAdvice`是Spring MVC框架中的一个注解，用于定义一个全局的异常处理器和绑定全局数据。它可以让开发者定义一个类，用于处理所有Controller层抛出的异常，
并且可以在该类中定义多个`@ExceptionHandler`方法，用于处理不同类型的异常。
具体来说，`@ControllerAdvice`注解的作用：
- 全局异常处理：定义`@ExceptionHandler`方法，处理Controller层抛出的异常。
- 全局数据绑定：定义`@ModelAttribute`方法，将数据绑定到全局，所有使用了`@RequestMapping`注解的方法都可以访问这些数据。
举个例子，假设我们定义了一个`GlobalExceptionHandler`类并使用`@ControllerAdvice`注解：
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    @ModelAttribute("globalData")
    public Map<String, Object> globalData() {
        Map<String, Object> data = new HashMap<>();
        data.put("appName", "My App");
        data.put("version", "1.0");
        return data;
    }
}
在这个例子中，我们定义了一个`handleException`方法，用于处理所有类型的异常，并返回一个包含错误信息的`ModelAndView`对象。
我们还定义了一个`globalData`方法，用于将一些全局数据绑定到所有Controller层中。
当Controller层抛出异常时，Spring MVC框架会自动调用`handleException`方法来处理异常，
并返回一个包含错误信息的`ModelAndView`对象。同时，所有Controller层都可以访问`globalData`方法返回的全局数据。*/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
/*@ControllerAdvice 注解中的 annotations 属性用于指定要处理的控制器类型。在这个例子中，我们指定了要处理 @RestController 和 @Controller 注解标记的控制器。*/
@ResponseBody
/*@ResponseBody 注解表示该方法的返回值会被序列化为 HTTP 响应体中的数据。
在这个例子中，我们使用 @ResponseBody 注解标记了一个方法，表示该方法的返回值会被序列化为 JSON 格式的数据，并放入 HTTP 响应中返回给客户端。*/
@Slf4j
public class GlobalExceptionHandler {
/*`@ExceptionHandler(SQLIntegrityConstraintViolationException.class)` 是Java Spring框架中的一个注解，用于处理控制器方法执行过程中发生的异常。
具体而言，`SQLIntegrityConstraintViolationException` 是一种特定类型的异常，当SQL语句违反主键、唯一键或外键约束时会发生这种异常。
例如，当尝试插入具有重复主键或引用不存在记录的外键时，就会发生这种异常。
通过使用 `@ExceptionHandler` 注解并将 `SQLIntegrityConstraintViolationException.class` 作为其参数，
我们可以告诉Spring捕获这种特定类型的异常，并以自定义方式处理它。这样，我们可以向用户提供更有意义的错误消息，或者采取其他适当的措施，例如回滚事务。*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {

        log.error(ex.getMessage());

         if(ex.getMessage().contains("Duplicate entry"))
         {
             String[] split = ex.getMessage().split(" ");
             String msg = split[2]+"已存在";
            return R.error(msg);
         }
        return R.error("未知错误");

    }


}
