package com.rewrite.selfexamsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  用户报名的实体类，对应数据库user_signup
 * @since 2022/7/26 11:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    /**
     * 用户名
     */
    private String username;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 专业层次
     */
    private String majorLevel;
    /**
     * 课程代码
     */
    private String courseCode;
    /**
     * 文件
     */
    private String file;
    /**
     * 审核结果：1未通过，-1不通过，0未审核，2未开始报名
     */
    private int examineResult;
}
