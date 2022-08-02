package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description: 用于管理Course_Code数据库的Mapper
 * @author: Levi
 * @date: 2022/5/7 13:48
 * @version: 3.0 (Created by Spring Boot)
 */


@Repository
@Mapper
public interface CourseCodeMapper {
    /**
     * @param course: 将Course Code插入数据库
     * @return: null
     * @description: 将得到的CourseCode插入到course_code数据库中
     * @author Levi
     * @date 2022/5/7 16:18
     */
    void insertCourse(Course course);

    /**
     * @param course: 需要删除的course数据
     * @return: 返回删除的条数
     * @description: 到数据库中删除course的数据
     * @author Levi
     * @date 2022/5/7 16:43
     */
    long deleteCourse(Course course);

    /**
     * @param courseDataMap: 通过Map将需要修改的数据，修改后的数据传进来
     * @return
     * @description: 修改数据库指定的course的数据
     * @author Levi
     * @date 2022/5/7 20:03
     */
    void updateCourse(Map<String, Object> courseDataMap);

    /**
     * @param: null
     * @return: 以列表形式返回所有课程以及代码
     * @description: 返回所有课程信息
     * @author Levi
     * @date 2022/5/7 20:57
     */
    List<Course> selectAllCourse();

    //    查询某一个课程：根据code
    Course selectCourseByCode(String code);
}
