package com.rewrite.selfexamsystem.domain;
//  管理员实体

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description :用于admin_data数据库的实体类
 * @since 2022/5/7 11:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    /**
     * 登录账号
     */
    private String username;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 权限
     */
    private int permisson;
    /**
     * note
     */
    private String note;

}
