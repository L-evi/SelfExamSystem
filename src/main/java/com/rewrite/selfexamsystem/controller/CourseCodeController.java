package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.domain.Course;
import com.rewrite.selfexamsystem.service.serviceImpl.CourseCodeServiceImpl;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description: 用于管理课程方面接口的Controller
 * @since 2022/5/7 16:11
 */
@RestController
@RequestMapping(value = "/courseCode")
public class CourseCodeController {
    @Autowired
    private CourseCodeServiceImpl courseCodeServiceImpl;

    /**
     * @param course: 需要加入的课程的详细信息
     * @return 返回加入课程是否成功过等提示信息
     * @description: 添加课程到数据库中
     * @author Levi
     * @since 2022/7/22 20:48
     */
    @RequestMapping(value = "/addCode", method = RequestMethod.POST)
    public ResponseResult addCourseCode(@RequestBody Course course) {
        return courseCodeServiceImpl.addCourseCode(course);
    }

    /**
     * @param course: 需要删除的课程信息
     * @return 返回删除课程的结果
     * @description: 传入删除课程的信息，删除后返回信息
     * @author Levi
     * @since 2022/7/22 21:04
     */
    @RequestMapping(value = "/deleteCode", method = RequestMethod.POST)
    public ResponseResult deleteCourseCode(@RequestBody Course course) {
        return courseCodeServiceImpl.deleteCourseCode(course);
    }

    /**
     * @param getMap: 通过Map接受前端的JSON数据
     * @return 返回课程是否修改成功等信息
     * @description: 将接收到新旧课程信息放进Map中在Service层中调用Mapper接口进行修改
     * @author Levi
     * @date 2022/5/7 20:18
     */
    @RequestMapping(value = "/modifyCode", method = RequestMethod.POST)
    public ResponseResult modifyCourseCode(@RequestBody Map<String, Object> getMap) {
        return courseCodeServiceImpl.modifyCourseCode(getMap);
    }

    /**
     * @param :
     * @return 返回所有的课程信息
     * @description : 查询所有的课程信息并且返回
     * @author Levi
     * @since 2022/7/22 21:47
     */
    @RequestMapping(value = "/showCode", method = RequestMethod.POST)
    public ResponseResult showAllCourseCode() {
        return courseCodeServiceImpl.showAllCourseCode();
    }
}
