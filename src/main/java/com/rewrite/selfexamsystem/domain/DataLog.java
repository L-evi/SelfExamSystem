package com.rewrite.selfexamsystem.domain;

import lombok.Data;

/**
 * @description: 操作记录的实体类data_log
 * @Author Levi
 * @Date 2022/5/8 0:23
 * @Version 3.0 (created by Spring Boot)
 */
@Data
public class DataLog {
    //    主键
    private Integer id;
    //    操作时间
    private String time;
    //    操作IP地址
    private String ip;
    //    操作者名称
    private String username;
    //    操作类型
    private String thing;
    //    操作者类型：UserInformation、Admin
    private String type;
    //    操作是否成功：成功 / 失败
    private String status;
    //    操作完成后具体信息
    private String res;
}
