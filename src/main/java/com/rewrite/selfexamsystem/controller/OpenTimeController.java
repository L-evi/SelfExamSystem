package com.rewrite.selfexamsystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.OpenTime;
import com.rewrite.selfexamsystem.service.serviceImpl.OpenTimeServiceImpl;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.util.JAXBSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description: 控制开放时间的接口，Controller，
 * @since 2022/5/7 15:47
 */
@RestController
@RequestMapping(value = "/openTime")
public class OpenTimeController {
    @Autowired
    private OpenTimeServiceImpl openTimeServiceImpl;

    /**
     * @param openTime:  获取前端设置的开放时间
     * @param headerMap: 获取token
     * @return 返回前端设置开放时间是否成功等提示信息
     * @description: 设置开放时间：管理员获取前端传回来的数据并且将其传回到Service进行设置开放时间
     * <a href="https://blog.csdn.net/gkx_csdn/article/details/88421994">String转化为timestamp</a>
     * @author Levi
     * @since 2022/7/22 17:05
     */

    @DataLogAnnotation(thing = "设置开放时间", peopleType = "admin")
    @RequestMapping(value = "/setOpenTime", method = RequestMethod.POST)
    public ResponseResult setOpenTime(@RequestBody OpenTime openTime, @RequestHeader Map<String, Object> headerMap) {
//        鉴权
        String token = (String) headerMap.get("token");
        try {
            JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
            String role = (String) jsonObject.get("role");
            if (!"admin".equals(role)) {
                jsonObject = new JSONObject();
                jsonObject.put("des", "权限不足无法设置开放时间");
                return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        设置开放时间
        return openTimeServiceImpl.setOpenTime(openTime);
    }

    /**
     * @param :
     * @return 返回开放时间及其其他信息
     * @description: 获取开放时间
     * @author Levi
     * @since 2022/7/22 17:19
     */
    @DataLogAnnotation(thing = "获取开放时间", peopleType = "user")
    @RequestMapping(value = "/getOpenTime", method = RequestMethod.POST)
    public ResponseResult getOpenTime() {
//        获取开放时间
        return openTimeServiceImpl.getOpenTime();
    }

    /**
     * @param headerMap 从中获取token
     * @return 返回前端清空开放时间的信息
     * @description: 清除开放时间：前端调用该接口后可以清空开放时间
     * <a href="https://blog.csdn.net/IT__learning/article/details/115026067">通过map获取token</a>
     * @author Levi
     * @since 2022/7/22 17:02
     */
    @DataLogAnnotation(thing = "清除开放时间", peopleType = "admin")
    @RequestMapping(value = "/deleteOpenTime", method = RequestMethod.POST)
    public ResponseResult deleteOpenTime(@RequestHeader Map<String, Object> headerMap) {
//        鉴权
        String token = (String) headerMap.get("token");
        try {
            JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
            String role = (String) jsonObject.get("role");
            if (!"admin".equals(role)) {
                jsonObject = new JSONObject();
                jsonObject.put("des", "权限不足无法清除开放时间");
                return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openTimeServiceImpl.deleteOpenTime();
    }

    /**
     * @param :
     * @return 返回判断结果
     * @description: 判断当前是否为开放时间：调用该接口可以判断当前时间是否为开放时间
     * @author Levi
     * @since 2022/7/22 17:47
     */
    @DataLogAnnotation(thing = "判断当前是否为开放时间", peopleType = "user")
    @RequestMapping(value = "/judgeOpenTime", method = RequestMethod.POST)
    public ResponseResult isOpenTime() {
        return openTimeServiceImpl.isOpenTime();
    }
}
