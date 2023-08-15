package com.czc.reggie_waimai.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {
/*serialVersionUID 是 Java 中的一个序列化版本号，用于确保序列化和反序列化的兼容性。
在 Java 中，当一个对象被序列化时，它的所有属性都会被写入字节流中。
当反序列化时，Java 虚拟机会使用类的 serialVersionUID 来验证序列化对象的版本是否与当前类的版本匹配。
如果版本不匹配，反序列化操作将失败并抛出一个 InvalidClassException 异常。因此，如果你的类可能会被序列化，
建议为该类显式地指定一个 serialVersionUID，以确保其兼容性。*/
    private static final long serialVersionUID = 1L;

    private Long id;


    //姓名
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
