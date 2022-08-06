package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Levi
 * @Date 2022/5/7 11:38
 * @Version 3.0 (created by Spring Boot)
 */
@Repository
@Mapper
public interface AdminMapper {
    /**
     * @param username: 用户名
     * @return 返回管理员Admin
     * @description : 通过用户名查询管理员：通过用户名查询管理员信息并返回
     * @author Levi
     * @since 2022/8/6 22:03
     */
    Admin selectByAdminUsername(String username);
}
