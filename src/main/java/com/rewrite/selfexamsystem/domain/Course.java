package com.rewrite.selfexamsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Levi
 * @version : 3.0 (Created by Spring Boot)
 * @description : 数据库course_code的对应实体类
 * @since : 2022/5/7 13:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    /**
     * 课程代码
     */
    private String code;
    /**
     * 课程名称
     */
    private String name;
}
