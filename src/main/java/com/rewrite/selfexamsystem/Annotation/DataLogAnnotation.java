package com.rewrite.selfexamsystem.Annotation;

import java.lang.annotation.*;

/**
 * @author : Levi
 * @version : 3.1 (created by Spring Boot)
 * @description : 用于日志类的自定义注解
 * @since : 2022/7/15 14:58
 */
@Documented
//  作用目标
@Target(value = ElementType.METHOD)
//  生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLogAnnotation {
    //    操作事件
    String thing() default "null";

    //    操作人类型
    String peopleType() default "null";
}
