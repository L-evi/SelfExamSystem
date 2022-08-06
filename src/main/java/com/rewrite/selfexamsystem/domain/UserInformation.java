package com.rewrite.selfexamsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description : 用于描述数据库user_information的实体类
 * @since 2022/5/7 11:42
 */

// 使用lombok依赖的@Data，能够搞定所有的getter和setter函数
@Data
@AllArgsConstructor
@NoArgsConstructor
// 用户的实体对象，每一个变量对应着数据库中的列属性（对应数据库user_information）
public class UserInformation {
    /**
     * 用户名（准考证号）
     */
    private String username;
    /**
     * 电话
     */
    private String tele;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private int xb;
    /**
     * 身份证号
     */
    private String sfzh;
}
