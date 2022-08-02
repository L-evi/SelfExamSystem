package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.domain.OpenTime;
import com.rewrite.selfexamsystem.service.serviceImpl.OpenTimeServiceImpl;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param openTime: 获取前端设置的开放时间
     * @return 返回前端设置开放时间是否成功等提示信息
     * @description: 获取前端传回来的数据并且将其传回到Service进行设置开放时间
     * @author Levi
     * @since 2022/7/22 17:05
     */
    //    string转化为timestamp：https://blog.csdn.net/gkx_csdn/article/details/88421994
    //    这里通过OpenTime中定义一个String token获取Token
    @RequestMapping(value = "/setOpenTime", method = RequestMethod.POST)
    public ResponseResult setOpenTime(@RequestBody OpenTime openTime) {
        return openTimeServiceImpl.setOpenTime(openTime);
    }

    /**
     * @param :
     * @return 返回开放时间及其其他信息
     * @description: 获取开放时间
     * @author Levi
     * @since 2022/7/22 17:19
     */
    @RequestMapping(value = "/getOpenTime", method = RequestMethod.POST)
    public ResponseResult getOpenTime() {
//        获取开放时间
        return openTimeServiceImpl.getOpenTime();
    }

    /**
     * @return 返回前端清空开放时间的信息
     * @description: 前端调用该接口后可以清空开放时间
     * @author Levi
     * @since 2022/7/22 17:02
     */
    //    清空开放时间：通过map获取token，参考链接：https://blog.csdn.net/IT__learning/article/details/115026067
    @RequestMapping(value = "/deleteOpenTime", method = RequestMethod.POST)
    public ResponseResult deleteOpenTime() {
        return openTimeServiceImpl.deleteOpenTime();
    }

    /**
     * @param :
     * @return 返回判断结果
     * @description: 调用该接口可以判断当前时间是否为开放时间
     * @author Levi
     * @since 2022/7/22 17:47
     */
    //    判断当前是否为开放时间
    @RequestMapping(value = "/judgeOpenTime", method = RequestMethod.POST)
    public ResponseResult isOpenTime() {
        return openTimeServiceImpl.isOpenTime();
    }
}
