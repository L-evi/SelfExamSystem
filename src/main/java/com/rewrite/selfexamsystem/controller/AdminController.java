package com.rewrite.selfexamsystem.controller;


import com.rewrite.selfexamsystem.service.serviceImpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description :  TODO 将所有的响应用ResponseResult封装
 * @since 2022/5/7 11:34
 */
@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminloginserviceimpl;
}
