package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.DataLog;
import com.rewrite.selfexamsystem.mapper.DataLogMapper;
import com.rewrite.selfexamsystem.service.DataLogService;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  历史操作Service的具体实现
 * @since 2022/7/24 23:22
 */
@Service
public class DataLogServiceImpl implements DataLogService {
    @Autowired
    private DataLogMapper dataLogMapper;

    /**
     * @param token: 用于鉴权
     * @return 返回响应体
     * @description : 获取所有日志：管理员获取所有日志数据并返回
     * @author Levi
     * @since 2022/8/6 22:16
     */
    @Override
    public ResponseResult ShowLog(String token) throws Exception {
//        解析token，获取其中的权限信息
        Claims claims = JwtUtil.parseJwt(token);
        JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
        String role = (String) jsonObject.get("role");
//        如果访问该接口的不是admin，就返回权限不足
        if (!role.equals("admin")) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足，无法查看历史操作");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        权限允许，获取所有的数据并且返回到前端
        List<DataLog> allDataLog = dataLogMapper.getAllDataLog();
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(allDataLog));
        Map<String, Object> res = new HashMap<>();
        res.put("des", "获取所有历史操作成功");
        jsonArray.add(res);
        return new ResponseResult(ResultCode.SUCCESS, jsonArray);
    }
}
