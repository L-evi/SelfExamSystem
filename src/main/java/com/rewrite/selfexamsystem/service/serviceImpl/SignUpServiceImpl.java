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

import java.util.HashMap;
import java.util.List;
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

    /**
     * @param token: 鉴权以及获取username
     * @return 返回用户报名状态信息
     * @description : 获取用户报名状态：用户通过username获取用户的报名状态
     * @author Levi
     * @since 2022/8/4 17:48
     */
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

    /**
     * @param token: 鉴权以及获取username
     * @return 返回用户报名信息
     * @description : 获取用户报名信息：用户通过username获取用户报名信息详情
     * @author Levi
     * @since 2022/8/4 17:52
     */
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

    /**
     * @param token:           鉴权以及获取username
     * @param files:           用户报名时一并上传的文件
     * @param signUp:          用户的报名信息
     * @param userInformation: 用户的个人信息
     * @return 返回用户信息是否上传成功等信息
     * @description : 上传用户报名信息：用户上传报名信息以及报名文件，并通过username存入到数据库中
     * @author Levi
     * @since 2022/8/4 17:54
     */
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

    /**
     * @param token:           鉴权以及获取username
     * @param files:           用户更新报名信息时一并上传的文件
     * @param signUp:          用户的报名信息
     * @param userInformation: 用户的个人信息
     * @return 返回用户信息是否更新成功等信息
     * @description : 更新用户报名信息：用户通过username更新报名信息到数据库中
     * @author Levi
     * @since 2022/8/4 17:54
     */
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

    /**
     * @param token: 鉴权以及获取username
     * @return 返回删除用户信息是否成功等信息
     * @description : 删除用户报名信息：用户通过username删除指定的用户报名信息，已审核的信息无法删除
     * @author Levi
     * @since 2022/8/4 17:56
     */
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

    /**
     * @param token: 鉴权
     * @return 返回报名人数
     * @description : 返回报名人数：管理员查询数据库获取报名人数
     * @author Levi
     * @since 2022/8/4 18:00
     */
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

    /**
     * @param getMap: 获取筛选信息以及搜索信息
     * @param token:  鉴权
     * @return 返回搜索报名信息的条数
     * @description :返回搜索的数量：管理员通过筛选或者搜索用户报名信息，返回搜索到信息的数量
     * @author Levi
     * @since 2022/8/4 18:02
     */
    @Override
    public ResponseResult adminGetSearchNumber(Map<String, Object> getMap, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法搜索用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        获取其中的信息
        String content = (String) getMap.get("content");
        String keyWords = (String) getMap.get("keyWords");
//       获取页码
        int page = Integer.parseInt((String) getMap.get("page"));
        int up = page * 10;
        int down = up - 10;
//        移除不必要的Key
        getMap.remove("page");
        getMap.remove("content");
        getMap.remove("keyWords");
//        将数据写入到map中
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put(content, keyWords);
        searchMap.put("up", up);
        searchMap.put("down", down);
        searchMap.putAll(getMap);
//        查找数据库
        List<Map<String, Object>> signUpInformation = signUpMapper.searchSignUpInformation(searchMap);
        jsonObject = new JSONObject();
        jsonObject.put("des", "查询用户报名信息数量成功");
        jsonObject.put("num", signUpInformation.size());
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param page:  数据分页，如果为-1则不需要分页，返回全部
     * @param token: 鉴权
     * @return 返回获取的用户报名信息
     * @description :获取用户报名信息：管理员获取报名信息，根据page进行分页
     * @author Levi
     * @since 2022/8/4 18:05
     */
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

    /**
     * @param getMap: 从中获取筛选关键词以及搜索的关键词
     * @param token:  鉴权
     * @return 返回筛选后以及搜索到的用户信息
     * @description : 搜索报名信息：管理员筛选以及搜索关键词获取用户报名信息
     * @author Levi
     * @since 2022/8/4 19:38
     */
    @Override
    public ResponseResult adminSearchInformation(Map<String, Object> getMap, String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
//        鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足无法搜索用户报名信息");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
//        获取其中的信息
        String content = (String) getMap.get("content");
        String keyWords = (String) getMap.get("keyWords");
//       获取页码
        int page = Integer.parseInt((String) getMap.get("page"));
        int up = page * 10;
        int down = up - 10;
//        移除不必要的Key
        getMap.remove("page");
        getMap.remove("content");
        getMap.remove("keyWords");
//        将数据写入到map中
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put(content, keyWords);
        searchMap.put("up", up);
        searchMap.put("down", down);
        searchMap.putAll(getMap);
//        查找数据库
        List<Map<String, Object>> signUpInformation = signUpMapper.searchSignUpInformation(searchMap);
        return new ResponseResult(ResultCode.SUCCESS, signUpInformation);
    }

    /**
     * @param signUp: 从中获取用户报名信息
     * @param token:  鉴权
     * @return 返回审核是否成功相关信息
     * @description : 审核用户报名信息：管理员审核用户信息并传入到数据库中
     * @author Levi
     * @since 2022/8/4 19:40
     */
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
