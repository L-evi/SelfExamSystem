package com.rewrite.selfexamsystem.controller.TestController;

import com.rewrite.selfexamsystem.Annotation.TestAnnotation;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Levi
 * @Date 2022/5/7 11:35
 * @Version 3.0 (created by Spring Boot)
 */

@RestController
public class Test_two {
    /**
     * @param : null
     * @return : Map<String, Object>: 以Map返回提示信息到前端
     * @author : Levi
     * @since : 2022/5/7 11:33
     */
    // 测试接口：GET请求，请求参数为空，返回字符串Hello World
    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public Map<String, Object> testHelloWorld() {
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "HelloWorld!");
        return res;
    }

    @TestAnnotation(value = "测试Annotation", content = "TestPost2")
    @RequestMapping(value = "/TestPost2", method = RequestMethod.POST)
    public Map<String, Object> testPostHelloWorld(@RequestBody Map<String, Object> getMap) {
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Post Hello World!");
        return res;
    }

    @RequestMapping(value = "/test/testResponeResult", method = RequestMethod.POST)
    public ResponseResult testResponseResult(@RequestBody Map<String, Object> getMap) {
        return new ResponseResult(ResultCode.SUCCESS, getMap);
    }
}
