package com.rewrite.selfexamsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  公告的实体类，对应announcement数据库
 * @since 2022/7/26 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    /**
     * 公告标题
     */
    private String title;
    /**
     * 公告发布时间
     */
    private String time;
    /**
     * 公告发布的作者
     */
    private String author;
    /**
     * 公告主体
     */
    private String body;
    /**
     * 公告附带文件的文件名
     */
    private String filename;
    /**
     * 是否发送邮件
     */
    private int isSendEmail;
}
