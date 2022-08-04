package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.domain.SignUp;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.service.SignUpService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  SignUp登记报名信息的Controller层
 * @since 2022/7/29 23:13
 */
@RestController
@RequestMapping(value = "signUp")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    /**
     * @param getMap 获取token
     * @return 返回用户报名状态
     * @description : 获取用户报名状态：获取token传到Service层获取用户报名状态
     * @author Levi
     * @since 2022/7/30 10:43
     */
    @RequestMapping(value = "/user/getStatus", method = RequestMethod.POST)
    public ResponseResult getUserStatus(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return signUpService.getUserStatus(token);
    }

    /**
     * @param getMap 从中获取token
     * @return 返回报名用户信息
     * @description : 查询用户信息：获取token传到Service层获取用户信息
     * @author Levi
     * @since 2022/7/30 10:45
     */
    @RequestMapping(value = "/user/getInformation", method = RequestMethod.POST)
    public ResponseResult getUserInformation(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return signUpService.getUserInformation(token);
    }

    /**
     * @param files           上传的文件
     * @param userInformation 用户信息
     * @param signUp          报名信息
     * @return 返回登记信息是否成功等信息
     * @description : 上传用户报名信息：将上传的信息传到Service层进行处理存储
     * @author Levi
     * @since 2022/7/30 11:04
     */
    @RequestMapping(value = "/user/uploadInformation", method = RequestMethod.POST)
    public ResponseResult uploadUserInformation(@RequestHeader Map<String, Object> headerMap, @RequestParam(value = "files") MultipartFile[] files, UserInformation userInformation, SignUp signUp) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.uploadUserInformation(token, files, signUp, userInformation);
    }

    /**
     * @param files:           上传的文件
     * @param userInformation: 用户信息
     * @param signUp:          报名信息
     * @return 返回修改信息是否成功等信息
     * @description : 修改用户报名信息：将上传的用户信息传Service层进行处理存储（逻辑上与上传用户报名信息一致，因此可以调用同一个接口）
     * @author Levi
     * @since 2022/7/30 11:10
     */
    @RequestMapping(value = "/user/modifyInformation", method = RequestMethod.POST)
    public ResponseResult modifyUserInformation(@RequestHeader Map<String, Object> headerMap, @RequestParam(value = "files") MultipartFile[] files, UserInformation userInformation, SignUp signUp) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.updateUserInformation(token, files, signUp, userInformation);
    }

    /**
     * @param getMap: 从Header中获取token
     * @return 返回删除用户报名信息是否成功等信息
     * @description : 删除用户报名信息：从将token传到Service层删除信息
     * @author Levi
     * @since 2022/7/30 11:18
     */
    @RequestMapping(value = "/user/deleteInformation", method = RequestMethod.POST)
    public ResponseResult deleteUserInformation(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return signUpService.deleteUserInformation(token);
    }

    /**
     * @param :
     * @return 返回报名人数
     * @description : 管理员获取报名人数：通过Service层返回的报名人数到前端
     * @author Levi
     * @since 2022/7/30 11:22
     */
    @RequestMapping(value = "/admin/getPersonNumber", method = RequestMethod.POST)
    public ResponseResult adminGetPersoonNumber(@RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.adminGetPersoonNumber(token);
    }

    /**
     * @param getMap:    从中获取筛选数据以及搜索数据
     * @param headerMap: 从中获取token
     * @return num：返回搜索到的报名信息的数量
     * @description : 搜索报名信息：通过关键词以及筛选词进行报名信息搜索，返回搜索到用户信息的数量
     * @author Levi
     * @since 2022/8/4 17:46
     */
    @RequestMapping(value = "/admin/getSearchNumber", method = RequestMethod.POST)
    public ResponseResult adminGetSearchNumber(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.adminGetSearchNumber(getMap, token);
    }

    /**
     * @param getMap:    其中获取page
     * @param headerMap: 从中获取token，鉴定是否为admin
     * @return 返回报名信息（分页）
     * @description :获取报名信息（分页）：返回指定页码的报名信息
     * @author Levi
     * @since 2022/7/30 17:37
     */
    @RequestMapping(value = "/admin/getInformation", method = RequestMethod.POST)
    public ResponseResult adminGetInformation(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) throws Exception {
        String page = (String) getMap.get("page");
        String token = (String) headerMap.get("token");
        return signUpService.adminGetInformation(page, token);
    }

    /**
     * @param getMap:    从中获取筛选数据以及搜索数据
     * @param headerMap: 从中获取token
     * @return List：返回搜索到或者筛选到的数据
     * @description : 搜索报名信息：通过关键词以及筛选词进行报名信息搜索
     * @author Levi
     * @since 2022/8/4 17:46
     */
    @RequestMapping(value = "/admin/searchInformation", method = RequestMethod.POST)
    public ResponseResult adminSearchInformation(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.adminSearchInformation(getMap, token);
    }

    /**
     * @param signUp:    从中获取examineResult
     * @param headerMap: 从中获取token
     * @return 返回审核结果是否已经输入到数据库中
     * @description : 获取username，修改对应的examineResult
     * @author Levi
     * @since 2022/7/30 17:42
     */
    @RequestMapping(value = "/admin/examine", method = RequestMethod.POST)
    public ResponseResult adminExamine(@RequestBody SignUp signUp, @RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return signUpService.adminExamine(signUp, token);
    }

}
