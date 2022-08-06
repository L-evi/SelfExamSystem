package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.OpenTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Levi
 * @since 2022/5/7 11:39
 * @description : open_time数据库的Mapper
 * @version  3.0 (created by Spring Boot)
 */

@Repository
@Mapper
public interface OpenTimeMapper {
    /**
     * @param id:        第几个开放时间
     * @param startTime: 开始时间
     * @param endTime:   结束时间
     * @return
     * @description : 添加开放时间：根据ID添加开放时间
     * @author Levi
     * @since 2022/8/6 22:09
     */
    void addOpenTime(int id, Timestamp startTime, Timestamp endTime);

    /**
     * @param
     * @return
     * @description : 清空开放时间：清空数据库中的开放时间
     * @author Levi
     * @since 2022/5/7 11:31
     */
    void clearOpenTime();

    /**
     * @param :
     * @return 返回开放时间
     * @description :获取最后一条数据：根据ID获取最后一段开放时间
     * @author Levi
     * @since 2022/8/6 22:08
     */
    //
    OpenTime getOpenTime();
}
