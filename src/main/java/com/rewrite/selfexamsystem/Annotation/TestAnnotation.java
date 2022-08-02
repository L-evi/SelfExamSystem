package com.rewrite.selfexamsystem.Annotation;

import java.lang.annotation.*;

/**
 * @description : 测试Annotation
 * @author Levi
 * @since 2022/7/14 16:48
 * @version 3.0 (created by Spring Boot)
 */
@Documented
@Target(value = ElementType.METHOD)
//  生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String value() default "";
    String content() default "";
}
