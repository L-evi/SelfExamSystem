package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.OpenTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * @Author Levi
 * @Date 2022/5/7 11:39
 * @Version 3.0 (created by Spring Boot)
 */

@Repository
@Mapper
public interface OpenTimeMapper {

    //    加入时间
    public void addOpenTime(int id, Timestamp startTime, Timestamp endTime);

    /**
     * @param : null
     * @return void
     * @author Levi
     * @date 2022/5/7 11:31
     */
    //    清空数据库：
    public void clearOpenTime();

    /**
     * @param : null
     * @return OpenTime
     * @author Levi
     * @date 2022/5/7 11:31
     */
    //    获取最后一条数据（最新时间），根据id
    public OpenTime getOpenTime();
}
