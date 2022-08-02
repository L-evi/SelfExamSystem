package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.Course;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

import java.util.Map;

/**
 * @description: 用于Course_Code服务提供接口
 * @author: Levi
 * @date: 2022/5/7 13:52
 * @version: 3.0 (Created by Spring Boot)
 */
public interface CourseCodeService {
    /**
     * @param course: 需要添加的course数据
     * @return: 返回是否添加成功
     * @description: 用于从course_code中添加course数据的函数
     * @author Levi
     * @date 2022/5/7 16:45
     */
    public ResponseResult addCourseCode(Course course);

    /**
     * @param course: 需要添加的course数据
     * @return: 返回是否添加成功
     * @description: 用于从course_code中添加course数据的函数
     * @author Levi
     * @date 2022/5/7 16:45
     */
    public ResponseResult deleteCourseCode(Course course);

    /**
     * @param courseDataMap: 将需要修改的数据以及修改后的数据以Map形式传入
     * @return
     * @description: 修改课程数据
     * @author Levi
     * @date 2022/5/7 20:10
     */
    public ResponseResult modifyCourseCode(Map<String, Object> courseDataMap);

    /**
     * @param :
     * @return: 以List返回所有课程信息
     * @description:
     * @author Levi
     * @date 2022/5/7 22:41
     */
    public ResponseResult showAllCourseCode();
}
