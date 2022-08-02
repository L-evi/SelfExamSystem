package com.rewrite.selfexamsystem.service;

/**
 * @Author Levi
 * @Date 2022/5/7 11:43
 * @Version 3.0 (created by Spring Boot)
 */
public interface AdminService {
    /**
     * @param account:  管理员账号
     * @param password: 管理员密码
     * @return boolean: 判断管理员是否能够登录
     * @author Levi
     * @date 2022/5/7 11:39
     */
    public boolean vertifyAdminLogin(String account, String password);
}
