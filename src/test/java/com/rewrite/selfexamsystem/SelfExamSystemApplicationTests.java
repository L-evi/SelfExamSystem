package com.rewrite.selfexamsystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.*;
import com.rewrite.selfexamsystem.mapper.*;
import com.rewrite.selfexamsystem.utils.FileUtil;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.MailUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RequiredArgsConstructor
class SelfExamSystemApplicationTests {

    @GetMapping("/one")
    public void methodOne(String name) {
    }

    @GetMapping("/two")
    public void methodTwo() throws InterruptedException {
        Thread.sleep(2000);
    }

    @GetMapping("/three")
    public void methodThree(String name, String age) {
    }

    @Test
    void contextLoads() {
        methodOne("one");
        try {
            methodTwo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        methodThree("three", "3");
    }

    @Autowired
    DataSource dataSource;

    //  获取数据库连接
    @Test
    void connectLoads() {
        try {
            System.out.println("获取的数据库连接是：" + dataSource.getConnection());
        } catch (SQLException throwables) {
            System.out.println("发生异常，获取数据库连接失败");
            throwables.printStackTrace();
        }
    }

    //    测试JWT
    @Test
    void testJWT() throws Exception {
//        放入JSON数据，方便校验
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "levi");
        jsonObject.put("role", "admin");
        String token = JwtUtil.createJwt(String.valueOf(jsonObject));
//        获取token
        System.out.println(token);
        Claims claims = JwtUtil.parseJwt(token);
//        解析token，并拿出其中的username字段
        jsonObject = JSONObject.parseObject(claims.getSubject());
        System.out.println(jsonObject.get("username"));
    }

    //    测试user_data数据库的Mapper功能
    @Autowired
    private LoginDataMapper loginDataMapper;

    @Test
    void testUserDataMapper() {
        System.out.println(loginDataMapper.selectUserDataByUsername("1"));
    }


    //    利用BCryptPassword生成密码
    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        $2a$10$lo0JMjHU7Ldf2Go26.w9uul7ezGDmqPsHOZhqJfMi/FS9pO.G/tlm
        String encode = bCryptPasswordEncoder.encode("123456");
        String encode2 = bCryptPasswordEncoder.encode("456789");
//        $2a$10$FatfpEOgeV8.nCMN5h2o4eeFH5GSrF5O8t4VhCgRqtQwlLdZOdT66
        System.out.println(encode);
        System.out.println(encode2);
        System.out.println(bCryptPasswordEncoder.matches("$2a$10$nA6DZZ7QmuBxI/.aH4aXxeD7jySwOyuFUKf.Mxaq.Df61ZB45JFYC", "123456"));
    }

    //    测试user_information的搜索功能
    @Autowired
    private UserInformationMapper userInformation;

    @Test
    public void testUserInformation() {
        System.out.println(userInformation.selectByUsername("1"));
    }

    //    测试：将对象转化为json
    @Test
    public void testClassToJson() {
        LoginData loginData = new LoginData("Levi", "123456");
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(loginData);
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.get("username"));
        System.out.println(jsonObject.get("password"));
    }

    //    测试：更新用户数据
    @Autowired
    private UserInformationMapper userInformationMapper;

    @Test
    public void testUpdateUserInformation() {
        UserInformation userInformation = new UserInformation("1", "13356402880", "2810292412@qq.com", "李", 0, "440782200111216519");
        System.out.println(userInformationMapper.updateUserInformation(userInformation));
    }

    //    测试：将list装换成JSONObject
    @Autowired
    private CourseCodeMapper courseCodeMapper;

    @Test
    public void testListToJSONObject() {
        JSONObject jsonObject = new JSONObject();
        List<Course> courseList = courseCodeMapper.selectAllCourse();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(courseList));
        Map<String, Object> res = new HashMap<>();
        res.put("des", "Test");
        jsonArray.add(res);
        System.out.println(jsonArray);
//        jsonObject = JSONObject.parseObject(courseList.toString());
        System.out.println(courseList);
    }

    //    测试：获取CourseCode中所有的数据，以List<Course>类型返回
    @Test
    public void testGetAllCourseCode() {
        List<Course> courseList = courseCodeMapper.selectAllCourse();
        for (Course course : courseList) {
            System.out.println(course);
        }
//        用这种方法就能够不用上面那种转换都能够输出List每一个Course的数据，直接使用getter函数即可
    }

    //    测试jwt中放入权限信息
    @Test
    public void testJWTAuthen() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "123");
        jsonObject.put("status", "User");
        String token = JwtUtil.createJwt(String.valueOf(jsonObject));
        System.out.println(token);
        String subject = JwtUtil.parseJwt(token).getSubject();
        jsonObject = JSONObject.parseObject(subject);
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("status"));
        System.out.println(subject);
    }

    //    测试：AdminMapper
    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void testAdminMapper() {
        System.out.println(adminMapper.selectByAdminUsername("123456"));
    }

    //    测试：解析Admin的token
    @Test
    public void testParseAdminToken() throws Exception {
        Claims claims = JwtUtil.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTY2Mzc4MGI4NzM0ODIzYjVlMWM2NTQwYTYxNmVmNyIsInN1YiI6IntcInJvbGVcIjpcImFkbWluXCIsXCJ1c2VybmFtZVwiOlwiMTIzNDU2XCJ9IiwiaXNzIjoiQ29tcHV0aW5nU2Nob29sIiwiaWF0IjoxNjU4NTk4Mzk3LCJleHAiOjE2NTg2MDkxOTd9.3prbH6zltet3XRm_RF30ljoXLU79B7kdXHeHI7DZnyg");
        System.out.println(claims.getSubject());
        JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("role"));
        System.out.println(jsonObject.get("username"));
    }

    //    测试：获取所有历史操作
    @Autowired
    private DataLogMapper dataLogMapper;

    @Test
    public void testGetAllDataLog() {
        List<DataLog> courseList = dataLogMapper.getAllDataLog();
        System.out.println(courseList);
        System.out.println(courseList.size());
        for (DataLog dataLog : courseList) {
            System.out.println(dataLog);
            System.out.println(dataLog.getThing());
        }
    }

    //    测试：使用Spring Mail发送纯文本邮件

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void testSendTextMail() {
/*        // 设定mail server
        senderImpl.setHost("smtp.qq.com");
        senderImpl.setProtocol("smtp");
        senderImpl.setUsername("3573897471@qq.com");
        senderImpl.setPassword("nhfgxvxltgalcjje");
        senderImpl.setPort(587);
        senderImpl.setDefaultEncoding("UTF-8");*/

//        定义邮箱操作主体
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        发件人
        simpleMailMessage.setFrom("3573897471@qq.com");
//        收件人
        simpleMailMessage.setTo("1377278306@qq.com");

//        发送的标题
        simpleMailMessage.setSubject("测试邮件");
//        发送的主要内容
        simpleMailMessage.setText("这个是一封测试邮件，测试Spring Mail功能，没有附带文件的发送邮件功能");


        javaMailSender.send(simpleMailMessage);
        System.out.println("success");
    }


    //    测试：使用Spring Mail发送带有文件的邮件
    @Test
    public void testSendFileMail() throws MessagingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom("3573897471@qq.com");
        mimeMessageHelper.setTo("1377278306@qq.com");
//        暗送人
        mimeMessageHelper.setBcc("暗送人@qq.com");
//        抄送人
        mimeMessageHelper.setCc("抄送人@qq.com");
//        发送的标题
        mimeMessageHelper.setSubject("测试邮件（带附件）");
//        发送的主要内容
        mimeMessageHelper.setText("这是一封测试邮件，测试Spring Mail功能，具有附带文件的发送邮件功能");
//        发送一张图片
        final String filepath = "src/main/resources/static/1658742133001.png";
        FileSystemResource fileSystemResource = new FileSystemResource(filepath);
//        添加附件文件
        mimeMessageHelper.addAttachment("1658742133001.png", fileSystemResource);
//        发送
        javaMailSender.send(mimeMailMessage);
    }

    //    测试：删除文件
    @Test
    public void testdeleteFiles() {
        System.out.println(FileUtil.deleteFile("src/main/resources/static/files/2/1658891431179.png"));
    }

    //    测试：@Value注解
    @Test
    public void testValue() {
        MailUtil mailUtil = new MailUtil();
        System.out.println(mailUtil.getSender());
        mailUtil.setSender("1121321654654");
        System.out.println(mailUtil.getSender());
    }

    //    测试：FileUtil工具类中如果上传文件为空数组结果如何
    @Test
    public void testFileUtilArrayIsNull() {
        MultipartFile[] files = new MultipartFile[1];
        System.out.println(FileUtil.MultipleFilesUpload(files, "2"));
    }

    //    测试：获取所有人的邮箱
    @Test
    public void testGetAllEmail() {
        List<String> allUserEmail = userInformationMapper.getAllUserEmail();
        String[] strings = allUserEmail.toArray(new String[0]);
        System.out.println(allUserEmail);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    //    测试：添加公告Mapper
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Test
    public void testAddAnnouncement() {
        Announcement announcement = new Announcement("标题", "2022-07-28 11:07:50", "作者", "主体", "文件路径", 1);
        announcementMapper.addAnnouncement(announcement);
    }

    //    测试：邮件工具类
    @Test
    public void testMailUtil() throws MessagingException {
        final String filepath = "C:\\Users\\13772\\OneDrive\\ProgramProject\\项目重构\\SelfExamSystem\\src\\main\\resources\\static\\files\\123456\\1658996021305.jpg|C:\\Users\\13772\\OneDrive\\ProgramProject\\项目重构\\SelfExamSystem\\src\\main\\resources\\static\\files\\123456\\1658996021307.jpg|C:\\Users\\13772\\OneDrive\\ProgramProject\\项目重构\\SelfExamSystem\\src\\main\\resources\\static\\files\\123456\\1658996021309.png|";
        String[] filespath = filepath.split("\\|");
        List<String> userEmail = userInformationMapper.getAllUserEmail();
        String[] strings = userEmail.toArray(new String[0]);
        MailUtil mailUtil = new MailUtil("3573897471@qq.com", strings, "测试标题", "测试标题" + "/n" + "测试作者" + "/n" + "测试主体");
        if (mailUtil.sendFileMail(filespath, javaMailSender)) {
            System.out.println("发送成功");
        } else {
            System.out.println("发送失败");
        }
        System.out.println("测试邮件");
    }

    //    测试：AnnouncementMapper的分页查询

    @Test
    public void testAnnouncementMapperPageSelect() {
        List<Announcement> announcementList = announcementMapper.showSomeAnnouncement(2, 0);
        System.out.println(announcementList);
    }


    //    测试：AnnouncementMapper获取公告数量
    @Test
    public void testAnnouncementMapperGetAnnouncementNumber() {
        System.out.println(announcementMapper.getAnnouncementNumber());
    }

    //    测试：AnnouncementMapper根据time和title获取Announcement
    @Test
    public void testGetAnnouncementByTimeTitle() {
        Announcement announcement = new Announcement();
        announcement.setTitle("测试标题1");
        announcement.setTime("2022-07-28 11:11:50");
        System.out.println(announcementMapper.getAnnouncement(announcement));
    }

    //    测试：AnnouncementMapper根据time和title删除Announcement
    @Test
    public void testDeleteAnnouncement() {
        Announcement announcement = new Announcement();
        announcement.setTitle("测试标题");
        announcement.setTime("2022-07-28 11:11:50");
        System.out.println(announcementMapper.deleteAnnouncement(announcement));
    }

    //    测试：AnnouncementMapper中根据keyWords模糊查询title和body并分页返回
    @Test
    public void testSearchAnnouncement() {
        List<Announcement> announcementList = announcementMapper.searchAnnouncement("主体2", 0, 20);
        System.out.println(announcementList);
        System.out.println(announcementList.size());
    }

    //    测试：AnnouncementMapper中根据关键词查询，返回查询到结果的数量
    @Test
    public void testSearchAnnouncementNumber() {
        System.out.println(announcementMapper.searchAnnouncementNumber("测试"));
    }

    //    测试：SignUpMapper中查询报名状态
    @Autowired
    private SignUpMapper signUpMapper;

    @Test
    public void testSignUpMapperGetStatus() {
        System.out.println(signUpMapper.getUserStatus("1"));
    }

    //    测试：获取用户报名信息
    @Test
    public void testSignUpUserInformation() {
        Map<String, Object> userSignUpInformation = signUpMapper.getUserSignUpInformation("1");
        System.out.println(userSignUpInformation);
    }

    //    测试：上传用户报名信息
    @Test
    public void testUploadSignUpInformation() {
        SignUp signUp = new SignUp("1", "专业姓名1", "专业层次1", "课程代码1", "文件1", 1);
        System.out.println(signUpMapper.uploadUserSignUpInformation(signUp));
    }

    //    测试：删除用户报名信息
    @Test
    public void testDeleteUserSignUpInformation() {
        System.out.println(signUpMapper.deleteUserInformation("1"));
    }

    //    测试：获取报名人数
    @Test
    public void testGetSignUpNumber() {
        int signUpPersonNumber = signUpMapper.getSignUpPersonNumber();
        System.out.println(signUpPersonNumber);
    }

    //    测试：获取全部的报名用户信息
    @Test
    public void testGetAllSignUpPerson() {
        List<Map<String, Object>> allSignUpPerson = signUpMapper.getAllSignUpPerson();
        for (Map<String, Object> getMap : allSignUpPerson) {
            System.out.println(getMap.toString());
        }
    }

    //    测试：获取一部分的报名用户信息
    @Test
    public void testGetSomeSignUpPerson() {
        List<Map<String, Object>> someSignUpPerson = signUpMapper.getSomeSignUpPerson(0, 8);
        System.out.println(someSignUpPerson.size());
        for (Map<String, Object> getMap : someSignUpPerson) {
            System.out.println(getMap.toString());
        }
    }

    //    测试：更新用户报名状态
    @Test
    public void testUpdateUserSignUpStatus() {
        SignUp signUp = new SignUp();
        signUp.setUsername("1");
        signUp.setExamineResult(1);
        System.out.println(signUpMapper.updateExamineResult(signUp));
    }

    //    测试：指定列查询
    @Test
    public void testColumnSelect() {
        Map<String, Object> getMap = new HashMap<>();
        final String content = "";
        String searchKey = null;
        getMap.put(content, searchKey);
        getMap.put("up", 10);
        getMap.put("down", 0);
        for (Map<String, Object> res : signUpMapper.searchSignUpInformation(getMap)) {
            System.out.println(res.toString());
        }
        System.out.println(signUpMapper.searchSignUpInformation(getMap).size());
    }

    //    测试：将Map中的数据放到另一个Map中
    @Test
    public void testMapPutAnotherMap() {
        Map<String, Object> getMap = new HashMap<>();
        getMap.put("des", "test");
        getMap.put("test", "des");
        Map<String, Object> anotherMap = new HashMap<>();
        anotherMap.putAll(getMap);
        System.out.println(getMap.toString());
        System.out.println(anotherMap.toString());
    }

    //    测试：从user_information数据库中获取指定username的邮箱
    @Test
    public void testGetEmailByUsername() {
        System.out.println(userInformationMapper.getEmail("1"));
        System.out.println(userInformationMapper.getEmail("100"));
    }
}
