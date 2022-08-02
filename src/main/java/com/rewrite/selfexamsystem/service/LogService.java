package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.DataLog;

/**
 * @description:
 * @Author Levi
 * @Date 2022/5/8 20:21
 * @Version 3.0 (created by Spring Boot)
 */
public interface LogService {
    int addLog(DataLog dataLog);
}
