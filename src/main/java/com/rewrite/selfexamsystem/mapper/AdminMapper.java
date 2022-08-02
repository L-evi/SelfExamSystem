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
    Admin selectByAdminUsername(String username);
}
