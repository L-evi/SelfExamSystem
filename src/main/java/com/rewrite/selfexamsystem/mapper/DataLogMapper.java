package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.DataLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: 管理data_log数据库的Mapper
 * @Author Levi
 * @Date 2022/5/8 0:30
 * @Version 3.0 (created by Spring Boot)
 */
@Repository
@Mapper
public interface DataLogMapper {

    /**
     * @param dataLog: 日志实体类
     * @return
     * @description : 添加日志：添加日志到数据库中
     * @author Levi
     * @since 2022/8/6 22:04
     */
    void addDataLog(DataLog dataLog);

    /**
     * @param :
     * @return 以列表形式返回日志实体类
     * @description : 查询所有日志：查询数据库中所有日志并返回List
     * @author Levi
     * @since 2022/8/6 22:05
     */
    List<DataLog> getAllDataLog();
}
