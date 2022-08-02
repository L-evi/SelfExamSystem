package com.rewrite.selfexamsystem.Annotation;

import java.lang.annotation.*;

/**
 * @description : 用于日志类的自定义注解
 * @author : Levi
 * @since : 2022/7/15 14:58
 * @version : 3.1 (created by Spring Boot)
 */
@Documented
//  作用目标
@Target(value = ElementType.METHOD)
//  生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLogAnnotation {
//    操作事件
    String thing() default "默认事件";
//    操作人类型
    String peopleType() default "默认操作人";
}
