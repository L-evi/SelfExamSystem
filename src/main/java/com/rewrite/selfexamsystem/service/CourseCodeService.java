package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.Course;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

import java.util.Map;

/**
 * @description: 用于Course_Code的Service提供接口
 * @author: Levi
 * @date: 2022/5/7 13:52
 * @version: 3.0 (Created by Spring Boot)
 */
public interface CourseCodeService {

    public ResponseResult addCourseCode(Course course);


    public ResponseResult deleteCourseCode(Course course);

    public ResponseResult modifyCourseCode(Map<String, Object> courseDataMap);


    public ResponseResult showAllCourseCode();
}
