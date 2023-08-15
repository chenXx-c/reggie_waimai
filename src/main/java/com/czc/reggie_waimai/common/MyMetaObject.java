package com.czc.reggie_waimai.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/*@Component是Spring框架中的一个注解，用于将一个类标识为一个组件，让Spring容器管理该组件的生命周期。被@Component注解标注的类会被自动扫描并注册到Spring容器中，可以通过Spring容器获取该组件的实例，从而使用该组件提供的功能。

@Component注解是一个泛化的概念，它可以用来标注任何类，但在实际使用时，通常使用更为具体的注解，如@Service、@Repository、@Controller等。这些注解都是@Component注解的派生注解，用于标识不同类型的组件，如@Service用于标识服务层组件，@Repository用于标识数据访问层组件，@Controller用于标识控制层组件等。

使用@Component注解可以让Spring容器自动管理组件的生命周期，包括创建、初始化和销毁等过程，从而简化开发工作，提高代码的可维护性和可重用性。*/
@Component
@Slf4j
/*MetaObjectHandler是MyBatis-Plus框架中的一个接口，
用于处理实体类对象在进行数据库操作时的自动填充功能。通过实现该接口并重写其中的方法，
可以实现在插入或更新实体类对象时，自动填充创建时间、更新时间、创建人、更新人等信息。
在使用MyBatis-Plus进行数据库操作时，可以通过配置GlobalConfig的metaObjectHandler
属性来指定使用哪个MetaObjectHandler实现类，从而实现自动填充功能。
通过使用MetaObjectHandler，可以简化开发工作，提高开发效率。*/
public class MyMetaObject implements MetaObjectHandler {
    //插入操作自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充insert");
        log.info(metaObject.toString());


        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("createUser", new Long(BaseContext.getCurrentId()));
        metaObject.setValue("updateUser", new Long(BaseContext.getCurrentId()));
    }

    //更新操作自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充update");

        log.info(metaObject.toString());

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", new Long(BaseContext.getCurrentId()));

    }
}
