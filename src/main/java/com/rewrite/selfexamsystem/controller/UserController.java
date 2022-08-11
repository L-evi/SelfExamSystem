package com.rewrite.selfexamsystem.controller;
// 用户注册Controller，负责与前端进行数据交换

import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.service.UserService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description: 用户的Controller层
 * @since 2022/5/7 11:37
 */
//采用Restful风格接受传输数据
@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {
    //    此处的类一定要
    @Autowired
    private UserService userService;

    /**
     * @param getMap:用于存储用户数据
     * @return ResponseResult自定义响应体
     * @description :用户注册：用户上传信息进行注册
     * 通过@ResqestBody来接受网页中的数据，注意接受的数据一定要和User对应起来
     * 此时produces = "application/json"，只能接受json数据，并且参数只能是一个或者用@ResponseBody注解的对象
     * <a href="https://www.cnblogs.com/jpfss/p/10966372.html">参考链接</a>
     * @author Levi
     * @since 2022/5/7 11:34
     */
    @DataLogAnnotation(thing = "用户注册", peopleType = "user")
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseResult UserRegister(@RequestBody Map<String, Object> getMap) {
//        参数写入
        LoginData loginData = new LoginData((String) getMap.get("username"), (String) getMap.get("password"));
        UserInformation userInformation = new UserInformation((String) getMap.get("username"), (String) getMap.get("tele"), (String) getMap.get("email"), (String) getMap.get("name"), Integer.parseInt((String) getMap.get("xb")), (String) getMap.get("sfzh"));
        return userService.UserRegisterService(userInformation, loginData);
    }


    /**
     * @param getMap: 从Header中获取token
     * @return 返回所需要的name和username以及提示信息
     * @description: 通过前端在Header中传回来的Token维持前端登录
     * @author Levi
     * @since 2022/7/21 22:26
     */
    @DataLogAnnotation(thing = "用户使用Token登录", peopleType = "user")
    @RequestMapping(value = "/tokenLogin", method = RequestMethod.POST)
    public ResponseResult UserTokenLogin(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return userService.UserTokenLogin(token);
    }

    /**
     * @param getMap: 从Header中获取token
     * @return 返回用户信息
     * @description: 通过Token中的解析出来的username查询到的用户信息返回到前端
     * @author Levi
     * @since 2022/7/21 23:34
     */
    @DataLogAnnotation(thing = "获取用户信息", peopleType = "user")
    @RequestMapping(value = "/personInfo", method = RequestMethod.POST)
    public ResponseResult UserGetPersonalInformation(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return userService.UserPersonalInformation(token);
    }

    /**
     * @param userInformation: 需要修改的用户数据
     * @param getMap:          用来接收Header中的token
     * @return 返回修改的结果
     * @description: 通过前端传入的数据，传给Service端进行修改后应答前端
     * @author Levi
     * @since 2022/7/21 23:37
     */
    @DataLogAnnotation(thing = "修改个人信息", peopleType = "user")
    @RequestMapping(value = "/modifyInformation", method = RequestMethod.POST)
    public ResponseResult UserModifyInformation(@RequestBody UserInformation userInformation, @RequestHeader Map<String, Object> getMap) throws Exception {
        return userService.UserModifyInformation(userInformation, (String) getMap.get("token"));
    }


    /**
     * @param getMap: 从中获取username
     * @return 返回发送验证邮件是否成功等信息
     * @description: 用户忘记密码：用户通过该接口发送邮箱验证码修改密码
     * @author Levi
     * @since 2022/7/22 11:34
     */
    @DataLogAnnotation(thing = "用户忘记密码", peopleType = "user")
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public ResponseResult userFindPassword(@RequestBody Map<String, Object> getMap) {
        String username = (String) getMap.get("username");
        return userService.userForgetPassword(username);
    }

    /**
     * @param getMap: 从中获取username、password、emailVerify参数
     * @return 返回修改密码是否成功等信息
     * @description : 用户重置密码：用户通过邮箱验证码通过验证后修改密码
     * @author Levi
     * @since 2022/8/6 19:57
     */
    @DataLogAnnotation(thing = "用户重置密码", peopleType = "user")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseResult userResetPassword(@RequestBody Map<String, Object> getMap) {
        LoginData loginData = new LoginData((String) getMap.get("username"), (String) getMap.get("password"));
        String emailVerify = (String) getMap.get("emailVerify");
        return userService.userResetPassword(loginData, emailVerify);
    }

}
