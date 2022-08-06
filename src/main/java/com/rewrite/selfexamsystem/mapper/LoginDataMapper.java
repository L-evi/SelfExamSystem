package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.LoginData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: Levi
 * @description: 数据库user_data的mapper
 * @since: 2022/7/19 23:12
 * @version: 3.1（Created By Spring Boot）
 */

@Repository
@Mapper
public interface LoginDataMapper {
    /**
     * @param username: 用户名
     * @return
     * @description : 登录：通过用户名查询登录用户信息
     * @author Levi
     * @since 2022/8/6 22:06
     */
    LoginData selectUserDataByUsername(String username);

    /**
     * @param loginData: 用户信息
     * @return
     * @description : 添加用户信息：添加登录用户信息到数据库中
     * @author Levi
     * @since 2022/8/6 22:07
     */
    void addUsernamePassword(LoginData loginData);

    /**
     * @param loginData: 用户信息
     * @return
     * @description : 更新用户信息：通过用户名更新用户信息
     * @author Levi
     * @since 2022/8/6 22:07
     */
    int updatePassword(LoginData loginData);
}
