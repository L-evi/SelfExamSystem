package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.Announcement;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description : 公告announcement的Service接口层
 * @since 2022/7/27 22:01
 */

public interface AnnouncementService {
    ResponseResult addAnnouncement(Announcement announcement, MultipartFile[] files, String token) throws Exception;

    ResponseResult showSomeAnnouncement(String page);

    ResponseResult showAllAnnouncement();

    ResponseResult searchAnnouncement(String keyWords, String page);

    ResponseResult getAnnouncementNumber();

    ResponseResult deleteAnnouncement(Announcement announcement,String token);

    ResponseResult getAnnouncement(Announcement announcement);

    ResponseResult searchAnnouncementNumber(String keyWords);
}
