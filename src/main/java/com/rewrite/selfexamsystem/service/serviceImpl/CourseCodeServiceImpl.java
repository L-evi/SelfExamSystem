package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.mapper.CourseCodeMapper;
import com.rewrite.selfexamsystem.domain.Course;
import com.rewrite.selfexamsystem.service.CourseCodeService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 用于实现CourseCodeService接口中的函数
 * @Author Levi
 * @Date 2022/5/7 16:13
 * @Version 3.0 (created by Spring Boot)
 */
@Service
public class CourseCodeServiceImpl implements CourseCodeService {
    @Autowired
    private CourseCodeMapper courseCodeMapper;

    /**
     * @param course: 需要添加到数据库的课程信息
     * @return 返回信息：1、数据库中已经有这一课程，添加课程信息失败   2、添加课程信息成功
     * @description: 将前端传回来的课程信息，判断数据库中是否有该课程信息，将课程信息添加到数据库中
     * @author Levi
     * @since 2022/7/22 20:49
     */
    @Override
    public ResponseResult addCourseCode(Course course) {
//        返回的信息
        JSONObject jsonObject = new JSONObject();
//        先查一下数据库里面有没有这一个数据
        Course tempCourse = courseCodeMapper.selectCourseByCode(course.getCode());
//        如果有这个数据就不用插入了
        if (!Objects.isNull(tempCourse)) {
            jsonObject.put("des", "插入失败，数据库中已经含有该课程信息");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
        courseCodeMapper.insertCourse(course);
        jsonObject.put("des", "插入课程信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param course: 需要删除的课程信息
     * @return 返回删除是否成功等提示信息
     * @description: 通过传入的课程信息删除对应数据
     * @author Levi
     * @since 2022/7/22 21:04
     */
    @Override
    public ResponseResult deleteCourseCode(Course course) {
//        返回的信息载体
        JSONObject jsonObject = new JSONObject();
        //        后续可以通过deleteCount返回删除的条数
        long deleteCount = courseCodeMapper.deleteCourse(course);
        if (deleteCount == 0) {
            jsonObject.put("des", "删除失败，数据库中无此数据");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
        jsonObject.put("des", "删除成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param courseDataMap : 将需要修改的数据以及修改后的数据以Map形式传入
     * @return 返回修改课程信息是否成功等信息
     * @description: 修改课程数据
     * @author Levi
     * @date 2022/5/7 20:10
     */
    @Override
    public ResponseResult modifyCourseCode(Map<String, Object> courseDataMap) {
        courseCodeMapper.updateCourse(courseDataMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "修改课程信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回所有课程信息
     * @description: 将所有的课程信息返回到前端
     * @author Levi
     * @since 2022/7/22 21:46
     */
    @Override
    public ResponseResult showAllCourseCode() {
        List<Course> courseList = courseCodeMapper.selectAllCourse();
        if (courseList == null || courseList.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "暂未查询到课程信息");
            return new ResponseResult(ResultCode.DATABASE_ERROR, jsonObject);
        }
        return new ResponseResult(ResultCode.SUCCESS, courseList);
    }
}
