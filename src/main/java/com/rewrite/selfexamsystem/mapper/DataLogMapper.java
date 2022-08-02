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
    //    添加日志
    void addDataLog(DataLog dataLog);

    //    获取所有操作记录
    List<DataLog> getAllDataLog();
}
