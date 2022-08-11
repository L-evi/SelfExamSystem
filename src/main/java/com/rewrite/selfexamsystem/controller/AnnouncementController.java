package com.rewrite.selfexamsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.Announcement;
import com.rewrite.selfexamsystem.service.AnnouncementService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  公告的Controller层
 * @since 2022/7/27 21:34
 */

@RestController
@CrossOrigin
@RequestMapping(value = "/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    /**
     * @param files:        上传的文件
     * @param getHeader:    从Header中获取token
     * @param announcement: 公告实体类用于接收参数
     * @return 返回响应体
     * @description : 上传公告：管理员前端传回文件以及其他参数，添加公告到数据库、并且决定是否发送邮件的操作
     * @author Levi
     * @since 2022/7/28 16:56
     */
    @DataLogAnnotation(thing = "上传公告", peopleType = "admin")
    @RequestMapping(value = "/addAnnouncement", method = RequestMethod.POST)
    public ResponseResult addAnnouncement(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestHeader Map<String, Object> getHeader, Announcement announcement) throws Exception {
        String token = (String) getHeader.get("token");
        return announcementService.addAnnouncement(announcement, files, token);
    }

    /**
     * @param getMap: 从其中获取需要公告的页数
     * @return 返回所需页数的公告
     * @description : 获取公告：通过前端的page返回对应页面的公告
     * @author Levi
     * @since 2022/7/29 16:25
     */
    @DataLogAnnotation(thing = "获取公告（分页）", peopleType = "user")
    @RequestMapping(value = "/showSomeAnnouncement", method = RequestMethod.POST)
    public ResponseResult showSomeAnnouncement(@RequestBody Map<String, Object> getMap) {
        String page = (String) getMap.get("page");
        return announcementService.showSomeAnnouncement(page);
    }

    /**
     * @param :
     * @return 返回所有公告信息
     * @description : 获取所有公告：返回前端所有的公告信息
     * @author Levi
     * @since 2022/7/29 16:26
     */
    @DataLogAnnotation(thing = "获取所有公告", peopleType = "user")
    @RequestMapping(value = "/showAllAnnouncement", method = RequestMethod.POST)
    public ResponseResult showAllAnnouncement() {
        return announcementService.showAllAnnouncement();
    }

    /**
     * @param getMap: 获取keyWords关键词以及page页数
     * @return 返回搜索到的公告或者是错误信息
     * @description : 搜索公告（分页）：根据关键词查询公告，并且分页返回
     * @author Levi
     * @since 2022/7/29 22:44
     */
    @DataLogAnnotation(thing = "搜索公告（分页）", peopleType = "uer")
    @RequestMapping(value = "/searchAnnouncement", method = RequestMethod.POST)
    public ResponseResult searchAnnouncement(@RequestBody Map<String, Object> getMap) {
        String keyWords = (String) getMap.get("keyWords");
        String page = (String) getMap.get("page");
        return announcementService.searchAnnouncement(keyWords, page);
    }

    /**
     * @param :
     * @return 返回公告的数量
     * @description : 获取公告数量：查询前端请求公告的数量
     * @author Levi
     * @since 2022/7/29 16:39
     */
    @DataLogAnnotation(thing = "获取公告数量", peopleType = "user")
    @RequestMapping(value = "/getAnnouncementNumber", method = RequestMethod.POST)
    public ResponseResult announcementNumber() {
        return announcementService.getAnnouncementNumber();
    }


    /**
     * @param announcement: 获取time和title
     * @return 返回是否删除成功的响应信息
     * @description : 删除公告：根据time和title删除指定公告，并返回提示信息
     * @author Levi
     * @since 2022/7/29 18:00
     */
    @DataLogAnnotation(thing = "删除公告", peopleType = "admin")
    @RequestMapping(value = "/deleteAnnouncement", method = RequestMethod.POST)
    public ResponseResult deleteAnnouncement(@RequestBody Announcement announcement, @RequestHeader Map<String, Object> headerMap) {
        String token = (String) headerMap.get("token");
        if (announcement.getTitle() == null || announcement.getTime() == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "缺少title或time，无法删除");
            return new ResponseResult(ResultCode.MISSING_PATAMETER, jsonObject);
        }
        return announcementService.deleteAnnouncement(announcement, token);
    }

    /**
     * @param announcement: 获取title和time
     * @return 返回查询到的Announcement信息
     * @description : 获取公告详细信息：根据前端给出的title和time查询指定的Announcement信息
     * @author Levi
     * @since 2022/7/29 17:40
     */
    @DataLogAnnotation(thing = "获取公告详细信息", peopleType = "user")
    @RequestMapping(value = "/getAnnouncement", method = RequestMethod.POST)
    public ResponseResult getAnnouncement(@RequestBody Announcement announcement) {
        if (announcement.getTitle() == null || announcement.getTime() == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "缺少title或time，无法查询");
            return new ResponseResult(ResultCode.MISSING_PATAMETER, jsonObject);
        }
        return announcementService.getAnnouncement(announcement);
    }

    /**
     * @param getmap: 获取关键词
     * @return 返回搜索到的公告的数量
     * @description : 查询搜索到的公告数量：根据关键词搜索并返回搜索到的公告的数量
     * @author Levi
     * @since 2022/7/29 23:06
     */
    @DataLogAnnotation(thing = "查询搜索到的公告数量", peopleType = "user")
    @RequestMapping(value = "/searchAnnouncementNumber", method = RequestMethod.POST)
    public ResponseResult searchAnnouncementNumber(@RequestBody Map<String, Object> getmap) {
        String keyWords = (String) getmap.get("keyWords");
        return announcementService.searchAnnouncementNumber(keyWords);
    }
}
