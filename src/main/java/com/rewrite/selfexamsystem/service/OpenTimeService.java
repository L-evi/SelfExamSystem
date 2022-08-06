package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.OpenTime;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @author Levi
 * @since 2022/5/7 11:44
 * @description : 开放时间open_time的Service接口
 * @version 3.0 (created by Spring Boot)
 */
public interface OpenTimeService {


    ResponseResult setOpenTime(OpenTime openTime);


    ResponseResult deleteOpenTime();


    ResponseResult getOpenTime();


    ResponseResult isOpenTime();
}
