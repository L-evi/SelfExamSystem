package com.rewrite.selfexamsystem.service.serviceImpl;

import com.rewrite.selfexamsystem.mapper.AdminMapper;
import com.rewrite.selfexamsystem.domain.Admin;
import com.rewrite.selfexamsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Levi
 * @Date 2022/5/7 11:43
 * @Version 3.0 (created by Spring Boot)
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminmapper;

    /**
     * @param account:
     * @param password:
     * @return boolean
     * @author Levi
     * @date 2022/5/7 11:27
     */
    @Override
    public boolean vertifyAdminLogin(String account, String password) {
/*        Admin tempAdmin = new Admin();
        tempAdmin = adminmapper.selectByAdminAccount(account, password);
//        如果为空，则说明登录失败
        if (tempAdmin == null) {
            return false;
        } else {
            return true;
        }*/
        return true;
    }
}
