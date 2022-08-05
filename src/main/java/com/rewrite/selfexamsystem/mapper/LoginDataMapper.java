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
    LoginData selectUserDataByUsername(String username);

    void addUsernamePassword(LoginData loginData);

    int updatePassword(LoginData loginData);
}
