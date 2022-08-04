package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.SignUp;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.mapper.SignUpMapper;
import com.rewrite.selfexamsystem.mapper.UserInformationMapper;
import com.rewrite.selfexamsystem.service.SignUpService;
import com.rewrite.selfexamsystem.utils.FileUtil;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  SignUp报名Service接口的实现类
 * @since 2022/7/29 23:14
 */
@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private SignUpMapper signUpMapper;

    @Autowired
    private UserInformationMapper userInformationMapper;

    @Override
    public ResponseResult getUserStatus(String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法获取报名状态");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
        String username = (String) jsonObject.get("username");
        int status = signUpMapper.getUserStatus(username);
        jsonObject = new JSONObject();
        if (status == 0) {
            jsonObject.put("res", "未审核");
        } else if (status == 1) {
            jsonObject.put("res", "审核通过");
        } else if (status == -1) {
            jsonObject.put("res", "审核不通过");
        } else {
            jsonObject.put("res", "尚未开始报名");
        }
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult getUserInformation(String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法获取用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
        String username = (String) jsonObject.get("username");
//        获取用户报名信息
        Map<String, Object> userSignUpInformation = signUpMapper.getUserSignUpInformation(username);
        userSignUpInformation.put("des", "查询用户报名信息成功");
        return new ResponseResult(ResultCode.SUCCESS, userSignUpInformation);
    }

    @Override
    public ResponseResult uploadUserInformation(String token, MultipartFile[] files, SignUp signUp, UserInformation userInformation) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法上传用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }

        String username = (String) jsonObject.get("username");
        //        上传文件
        Map<String, Object> filesUpload = FileUtil.MultipleFilesUpload(files, username);
        if (filesUpload.get("msg").equals("fail")) {
            jsonObject = new JSONObject();
            jsonObject.put("des", filesUpload.get("des"));
            return new ResponseResult(ResultCode.IO_OPERATION_ERROR, jsonObject);
        }
        //        获取文件上传路径
        String filespath = (String) filesUpload.get("filepath");
//        设置文件上传路径
        signUp.setFile(filespath);
//        查询数据库中是否有该数据
        if (!Objects.isNull(signUpMapper.getUserSignUpInformation(username))) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "数据库中已经有该数据，上传失败");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
//        设置用户名
        userInformation.setUsername(username);
        signUp.setUsername(username);
//        设置报名状态：未审核
        signUp.setExamineResult(0);
//        上传用户信息
        userInformationMapper.updateUserInformation(userInformation);
        signUpMapper.uploadUserSignUpInformation(signUp);
//        返回信息
        jsonObject = new JSONObject();
        jsonObject.put("des", "上传用户信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult updateUserInformation(String token, MultipartFile[] files, SignUp signUp, UserInformation userInformation) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法更新用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        获取用户名
        String username = (String) jsonObject.get("username");
//        获取报名状态：审核通过状态下无法更新信息
        int userStatus = signUpMapper.getUserStatus(username);
        if (userStatus == 1) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "该用户报名审核已经通过，无法修改用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
        //        上传文件
        Map<String, Object> filesUpload = FileUtil.MultipleFilesUpload(files, username);
        if (filesUpload.get("msg").equals("fail")) {
            jsonObject = new JSONObject();
            jsonObject.put("des", filesUpload.get("des"));
            return new ResponseResult(ResultCode.IO_OPERATION_ERROR, jsonObject);
        }
        //        获取文件上传路径
        String filespath = (String) filesUpload.get("filepath");
//        设置文件上传路径
        signUp.setFile(filespath);
//        查询数据库中是否有该数据
        if (Objects.isNull(signUpMapper.getUserSignUpInformation(username))) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "数据库中没有该数据，更新失败");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
//        设置用户名
        userInformation.setUsername(username);
        signUp.setUsername(username);
//        更新用户信息
        userInformationMapper.updateUserInformation(userInformation);
        signUpMapper.updateUserSignUpInformation(signUp);
//        返回信息
        jsonObject = new JSONObject();
        jsonObject.put("des", "更新用户信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult deleteUserInformation(String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法删除用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        获取用户名
        String username = (String) jsonObject.get("username");
//        获取用户报名状态：只有审核不通过的情况下才能删除
        int userStatus = signUpMapper.getUserStatus(username);
        if (userStatus != -1) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "用户报名信息尚未审核或已审核通过，无法删除报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        删除
        if (signUpMapper.deleteUserInformation(username) == 0) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "删除用户报名信息失败");
            return new ResponseResult(ResultCode.DATABASE_ERROR, jsonObject);
        }
        jsonObject = new JSONObject();
        jsonObject.put("des", "删除用户报名信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult adminGetPersoonNumber(String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法获取报名人数");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        返回信息
        jsonObject = new JSONObject();
        jsonObject.put("num", signUpMapper.getSignUpPersonNumber());
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult adminGetSearchNumber() {
        return null;
    }

    @Override
    public ResponseResult adminGetInformation(String page, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法获取报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        获取页码，如果page = -1 则返回所有
        int pageInt = Integer.parseInt(page);
        if (pageInt == -1) {
            return new ResponseResult(ResultCode.SUCCESS, signUpMapper.getAllSignUpPerson());
        }
//        如果不为-1，则返回十个为一页
        int up = pageInt * 10;
        int down = up - 10;
        return new ResponseResult(ResultCode.SUCCESS, signUpMapper.getSomeSignUpPerson(down, up));
    }

    @Override
    public ResponseResult adminSearchInformation(Map<String, Object> getMap, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        return null;
    }

    @Override
    public ResponseResult adminExamine(SignUp signUp, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法审核报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        修改数据库数据
        if (signUpMapper.updateExamineResult(signUp) == 0) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "审核用户报名信息失败");
            return new ResponseResult(ResultCode.DATABASE_ERROR, jsonObject);
        }
        jsonObject = new JSONObject();
        jsonObject.put("des", "审核用户报名信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }
}
