package com.rewrite.selfexamsystem.controller;


import com.rewrite.selfexamsystem.service.serviceImpl.AdminServiceImpl;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description :
 * @since 2022/5/7 11:34
 */
@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminloginserviceimpl;

    @RequestMapping(value = "/admin/tokenLogin", method = RequestMethod.POST)
    public ResponseResult adminTokenLogin(@RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return adminloginserviceimpl.adminTokenLogin(token);
    }
}
