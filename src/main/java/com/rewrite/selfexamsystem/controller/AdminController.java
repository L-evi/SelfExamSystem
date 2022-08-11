package com.rewrite.selfexamsystem.controller;


import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.service.serviceImpl.AdminServiceImpl;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description :
 * @since 2022/5/7 11:34
 */
@RestController
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminServiceImpl adminloginserviceimpl;

    /**
     * @param headerMap: 从中获取token
     * @return 返回token登录是否成功等信息
     * @description : 管理员Token登录：从header中获取token然后重新登陆，用于刷新页面时可以保持登陆状态
     * @author Levi
     * @since 2022/8/5 11:01
     */
    @DataLogAnnotation(thing = "管理员通过token登录", peopleType = "admin")
    @RequestMapping(value = "/admin/tokenLogin", method = RequestMethod.POST)
    public ResponseResult adminTokenLogin(@RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return adminloginserviceimpl.adminTokenLogin(token);
    }
}
