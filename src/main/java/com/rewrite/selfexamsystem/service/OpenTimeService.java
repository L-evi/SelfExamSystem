package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.OpenTime;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @Author Levi
 * @Date 2022/5/7 11:44
 * @Version 3.0 (created by Spring Boot)
 */
public interface OpenTimeService {


    //    添加开放时间
    ResponseResult setOpenTime(OpenTime openTime);

    /**
     * @param : null
     * @return boolean: 判断是否清空数据库成功
     * @author Levi
     * @date 2022/5/7 11:42
     */
    //    清空开放时间
    ResponseResult deleteOpenTime();

    /**
     * @param : null
     * @return OpenTime: 返回开放时间段
     * @author Levi
     * @date 2022/5/7 11:42
     */
    //    获取开放时间
    ResponseResult getOpenTime();

    /**
     * @param :null
     * @return boolean: 判断是否为开放时间
     * @author Levi
     * @date 2022/5/7 11:43
     */
    //    判断当前是否为开放时间
    ResponseResult isOpenTime();
}
