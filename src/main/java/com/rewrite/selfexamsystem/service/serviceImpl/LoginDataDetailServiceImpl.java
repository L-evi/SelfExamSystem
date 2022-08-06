package com.rewrite.selfexamsystem.service.serviceImpl;

import com.rewrite.selfexamsystem.domain.Admin;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.mapper.AdminMapper;
import com.rewrite.selfexamsystem.mapper.LoginDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: Levi
 * @description: 利用Spring Security的功能，继承了接口UserDetailsService，能够进行权限认证
 * @since: 2022/7/19 22:01
 * @version: 3.1（Created By Spring Boot）
 */

@Service
public class LoginDataDetailServiceImpl implements UserDetailsService {
    @Autowired
    private LoginDataMapper loginDataMapper;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * @param username: 传入用户名String，通过该关键字查询信息
     * @return 返回UserDetails，用户的详细信息
     * @description: 实现SpringSecurity中的接口UserDetailsService，能够从数据库中查询用户信息进行验证，而不是从内存中
     * @author Levi
     * @since 2022/7/19 22:02
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] strings = username.split("\\|");
        LoginData loginData = null;
//        因为返回类型UserDetails是一个接口，因此我们要声明一个实体类LoginData继承这个变量
        if (strings[1].equals("user")) {
            loginData = loginDataMapper.selectUserDataByUsername(strings[0]);
        } else if (strings[1].equals("admin")) {
            Admin admin = adminMapper.selectByAdminUsername(strings[0]);
            loginData = new LoginData(admin.getUsername(), admin.getPassword());
        }
//        如果查询不到数据就返回错误
        if (Objects.isNull(loginData)) {
            throw new RuntimeException("用户名或密码错误");
        }
//        TODO 根据用户权限信息，添加到LoginData中（暂时不需要添加）
//        封装成UserDetails对象返回
        return loginData;
    }
}
