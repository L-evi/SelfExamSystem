package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.service.DataLogService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description : 历史操作的Controller
 * @since 2022/7/24 23:13
 */

@RestController
@RequestMapping(value = "/dataLog")
public class DataLogController {
    @Autowired
    private DataLogService dataLogService;

    @RequestMapping(value = "/showLog", method = RequestMethod.POST)
    public ResponseResult ShowLog(@RequestHeader Map<String, Object> getMap) throws Exception {
        String token = (String) getMap.get("token");
        return dataLogService.ShowLog(token);
    }
}
