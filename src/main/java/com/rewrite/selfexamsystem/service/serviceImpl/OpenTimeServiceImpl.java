package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.mapper.OpenTimeMapper;
import com.rewrite.selfexamsystem.domain.OpenTime;
import com.rewrite.selfexamsystem.service.OpenTimeService;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Levi
 * @version 3.1 (created by Spring Boot)
 * @description : TODO 将数据加入到redis中
 * @since 2022/7/22 16:57
 */

@Service
public class OpenTimeServiceImpl implements OpenTimeService {
    @Autowired
    private OpenTimeMapper openTimeMapper;

    //      RedisCache
    @Autowired
    private RedisCache redisCache;

    /**
     * @param openTime: 前端传入需要修改的时间
     * @return 返回修改的结果
     * @description: 将前端传入的数据修改开放时间，并且将新数据放入redis中
     * @author Levi
     * @since 2022/7/22 16:59
     */
    @Override
    public ResponseResult setOpenTime(OpenTime openTime) {
//        将String转化成Timestamp
        Timestamp startTime = Timestamp.valueOf(openTime.getBegin());
        Timestamp endTime = Timestamp.valueOf(openTime.getEnd());
//        查询最新的开放时间获取其ID
        OpenTime lastOpenTime = openTimeMapper.getOpenTime();
        int id;
        if (Objects.isNull(lastOpenTime)) {
            id = 0;
        } else {
            id = lastOpenTime.getId();
        }
//        修改开放时间
        openTimeMapper.addOpenTime(id + 1, startTime, endTime);
//      修改成功后把开放时间放入redis中
//        返回成功信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "设置开放时间成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回是否成功获取等信息
     * @description: 从数据库获取最新开放时间，并且判断是否成立
     * @author Levi
     * @since 2022/7/22 17:19
     */
    //    获取数据库第一条数据：获取开放时间
    @Override
    public ResponseResult getOpenTime() {
//        获取最新的开放时间
        OpenTime openTime = openTimeMapper.getOpenTime();
//        返回信息
        JSONObject jsonObject = new JSONObject();
//        判断是否为空
        if (Objects.isNull(openTime)) {
            jsonObject.put("des", "获取最新开放时间失败，不存在此数据");
            return new ResponseResult(ResultCode.SYSTEM_ERROR, jsonObject);
        }
//        如果不为空则放入数据
        jsonObject = (JSONObject) JSON.toJSON(openTime);
        jsonObject.put("des", "获取最新开放时间成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回清空数据库成功与否的提示信息
     * @description: 清空数据库，清空开放时间
     * @author Levi
     * @since 2022/7/22 17:07
     */
    //    清空数据库：清除开放时间
    @Override
    public ResponseResult deleteOpenTime() {
        openTimeMapper.clearOpenTime();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "清空开放时间成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回是否开放时间的判断结果
     * @description: 根据数据库中的最新开放时间，判断当前时间是否为开放时间
     * @author Levi
     * @since 2022/7/22 17:48
     */
    @Override
    public ResponseResult isOpenTime() {
//        设置返回的信息
        JSONObject jsonObject = new JSONObject();
//        通过OpenTime结构体获取数据库中的数据
        OpenTime openTime = openTimeMapper.getOpenTime();
        if (Objects.isNull(openTime)) {
            jsonObject.put("des", "尚未设置开放时间");
            return new ResponseResult(ResultCode.SYSTEM_ERROR, jsonObject);
        }
//        将String转化为Date
//        参考链接：https://blog.csdn.net/feiyu_csdn/article/details/78782465
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = new Date();
        Date endTime = new Date();
//        获取当前时间
        Date nowTime = new Date();
//        尝试转换并且捕获错误
        try {
            beginTime = sdf.parse(openTime.getBegin());
            endTime = sdf.parse(openTime.getEnd());
        } catch (Exception e) {
            System.out.println("时间转换异常");
            e.printStackTrace();
        }
        //        判断当前时间在不在时间段内
        if (nowTime.after(beginTime) && nowTime.before(endTime)) {
            jsonObject.put("des", "当前时间在开放时间内");
            jsonObject.put("status", "1");
            return new ResponseResult(ResultCode.SUCCESS, jsonObject);
        }
        jsonObject.put("des", "当前时间不在开放时间内");
        jsonObject.put("status", "0");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }
}
