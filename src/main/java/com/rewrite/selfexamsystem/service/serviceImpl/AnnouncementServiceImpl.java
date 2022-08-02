package com.rewrite.selfexamsystem.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.Announcement;
import com.rewrite.selfexamsystem.mapper.AnnouncementMapper;
import com.rewrite.selfexamsystem.mapper.UserInformationMapper;
import com.rewrite.selfexamsystem.service.AnnouncementService;
import com.rewrite.selfexamsystem.utils.FileUtil;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.MailUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  公告的Service实现层
 * @since 2022/7/27 22:01
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private UserInformationMapper userInformationMapper;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * @param announcement: 公告实体类
     * @param files:        需要上传的文件
     * @param token:        鉴权码
     * @return 返回响应体
     * @description : 通过前端的数据，将文件存储下来，并且选择是否发送邮件，然后把公告数据放入数据库
     * @author Levi
     * @since 2022/7/28 16:43
     */
    @Override
    public ResponseResult addAnnouncement(Announcement announcement, MultipartFile[] files, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        检查是不是管理员，不是返回权限不足
        String role = (String) jsonObject.get("role");
        if (!role.equals("admin")) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "用户权限不足");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        把文件存起来，获取路径
        Map<String, Object> fileDetails = new HashMap<>();
        if (files != null) {
            fileDetails = FileUtil.MultipleFilesUpload(files, (String) jsonObject.get("username"));
//        如果存储失败
            if ("fail".equals(fileDetails.get("msg"))) {
                jsonObject = new JSONObject();
                jsonObject.put("des", fileDetails.get("des"));
                return new ResponseResult(ResultCode.IO_OPERATION_ERROR, jsonObject);
            }
        }
//        获取文件路径并注入对象中
        if (!fileDetails.isEmpty()) {
            String filepath = (String) fileDetails.get("filepath");
            announcement.setFilename(filepath);
        }
//        判断是否需要发送邮件
        if (announcement.getIsSendEmail() != 0) {
//            获取所有的用户的邮箱
            List<String> allEmail = userInformationMapper.getAllUserEmail();
            String[] recipients = (String[]) allEmail.toArray(new String[0]);
//            构建邮件工具类
            MailUtil mailUtil = new MailUtil("3573897471@qq.com", recipients, announcement.getTitle(), announcement.getTitle() + "\n" + announcement.getAuthor() + "\n" + announcement.getBody());
//            如果没有文件则发送文本邮件，如果有则发送附件邮件
            if (fileDetails.isEmpty()) {
                if (!mailUtil.sendTextMail(mailSender)) {
                    jsonObject = new JSONObject();
                    jsonObject.put("des", "发送文本邮件失败");
                    return new ResponseResult(ResultCode.SYSTEM_ERROR, jsonObject);
                }
            } else {
                String[] filespath = announcement.getFilename().split("\\|");
                if (mailUtil.sendFileMail(filespath, mailSender)) {
                    jsonObject = new JSONObject();
                    jsonObject.put("des", "发送文本邮件失败");
                    return new ResponseResult(ResultCode.SYSTEM_ERROR, jsonObject);
                }
            }
        }
//        将数据存入数据库中
        announcementMapper.addAnnouncement(announcement);
//        返回成功信息
        jsonObject = new JSONObject();
        jsonObject.put("des", "公告添加成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param page: 所需公告的页数
     * @return 返回对应页数的公告
     * @description : 通过前端传回来的page，返回对应页数的公告，十个数据一页
     * @author Levi
     * @since 2022/7/29 16:40
     */
    @Override
    public ResponseResult showSomeAnnouncement(String page) {
//        获取上下界
        int up = Integer.parseInt(page) * 10;
        int down = up - 10;
//        分页获取公告信息
        List<Announcement> announcementList = announcementMapper.showSomeAnnouncement(down, up);
//        返回公告信息
        if (announcementList == null || announcementList.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "相应页面暂无公告信息");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
        return new ResponseResult(ResultCode.SUCCESS, announcementList);
    }

    /**
     * @param :
     * @return List：返回所有的Announcement信息
     * @description : 返回前端所有的公告信息
     * @author Levi
     * @since 2022/7/28 23:09
     */
    @Override
    public ResponseResult showAllAnnouncement() {
        List<Announcement> announcementList = announcementMapper.showAllAnnouncement();
        JSONObject jsonObject = new JSONObject();
        if (announcementList == null || announcementList.isEmpty()) {
            jsonObject.put("des", "暂未查询到公告信息");
            return new ResponseResult(ResultCode.DATABASE_ERROR, jsonObject);
        }
        return new ResponseResult(ResultCode.SUCCESS, announcementList);
    }

    /**
     * @param keyWords: 关键词
     * @param page:     所需的页码
     * @return 返回搜索结果或者是错误信息
     * @description : 查找公告（分页）：通过关键词对公告中的title和body中的内容进行查询，并且分页返回
     * @author Levi
     * @since 2022/7/29 22:45
     */
    @Override
    public ResponseResult searchAnnouncement(String keyWords, String page) {
//        获取上下界
        int up = Integer.parseInt(page) * 10;
        int down = up - 10;
//      根据关键词分页获取公告
        List<Announcement> announcementList = announcementMapper.searchAnnouncement(keyWords, down, up);
//        判断有没有数据
        if (announcementList == null || announcementList.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "暂未搜寻到公告");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
//        返回搜寻到的公告数据
        return new ResponseResult(ResultCode.SUCCESS, announcementList);
    }


    /**
     * @param :
     * @return 返回响应体，其中包括公告的总数量
     * @description : 查询公告的总数量
     * @author Levi
     * @since 2022/7/29 16:39
     */
    @Override
    public ResponseResult getAnnouncementNumber() {
        JSONObject jsonObject = new JSONObject();
        int announcementNumber = announcementMapper.getAnnouncementNumber();
        jsonObject.put("des", "查询公告数量成功");
        jsonObject.put("num", announcementNumber);
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param announcement: 从中获取title和time
     * @return 返回是否删除成功的提示信息
     * @description : 删除公告：根据title和time删除公告，返回响应体信息
     * @author Levi
     * @since 2022/7/29 18:01
     */
    @Override
    public ResponseResult deleteAnnouncement(Announcement announcement) {
//        返回的信息
        JSONObject jsonObject = new JSONObject();
        if (announcementMapper.deleteAnnouncement(announcement) == 0) {
            jsonObject.put("des", "删除失败，尚未查询到有该公告信息");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
        jsonObject.put("des", "删除成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param announcement: 获取其中的title和time
     * @return 返回响应体，其中携带查询到的Announcement信息
     * @description : 通过title和time查询Announcement信息，并且根据是否查询到返回对应响应到前端
     * @author Levi
     * @since 2022/7/29 17:41
     */
    @Override
    public ResponseResult getAnnouncement(Announcement announcement) {
//        返回的信息
        JSONObject jsonObject = new JSONObject();
        Announcement mapperAnnouncement = announcementMapper.getAnnouncement(announcement);
//        判断查询到的对象是不是存在
        if (Objects.isNull(mapperAnnouncement)) {
            jsonObject.put("des", "暂未查询到该公告信息");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
//        返回响应信息
        jsonObject = (JSONObject) JSON.toJSON(mapperAnnouncement);
        jsonObject.put("des", "查询公告成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param keyWords: 关键词
     * @return 返回查询到的公告的数量
     * @description : 根据关键词对title和body进行查询，返回查询的结果的数量
     * @author Levi
     * @since 2022/7/29 23:08
     */
    @Override
    public ResponseResult searchAnnouncementNumber(String keyWords) {
        int count = announcementMapper.searchAnnouncementNumber(keyWords);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "查询公告成功");
        jsonObject.put("num", count);
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }
}
