package com.rewrite.selfexamsystem.domain;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Levi
 * @version 3.1 (created by Spring Boot)
 * @description : 开放时间的实体类
 * @since 2022/7/22 16:40
 */
// 自动生成setter和getter等函数
@Data
public class OpenTime {
    //    id
    private int id;
    //    开始时间
    private String begin;
    //    结束时间
    private String end;
}
