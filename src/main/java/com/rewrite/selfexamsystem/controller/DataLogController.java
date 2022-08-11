package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.service.DataLogService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.lang.management.ThreadInfo;
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

    /**
     * @param headerMap: 从中获取token
     * @return 返回所有日志信息
     * @description : 展示所有日志：管理员查询获取所有的日志信息
     * @author Levi
     * @since 2022/8/6 17:55
     */
    @DataLogAnnotation(thing = "展示所有日志", peopleType = "admin")
    @RequestMapping(value = "/showLog", method = RequestMethod.POST)
    public ResponseResult ShowLog(@RequestHeader Map<String, Object> headerMap) throws Exception {
        String token = (String) headerMap.get("token");
        return dataLogService.ShowLog(token);
    }
}
