package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.SignUp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Levi
 * @description: TODO
 * @since: 2022/7/29 23:15
 * @version: 3.1（Created By Spring Boot）
 */
@Repository
@Mapper
public interface SignUpMapper {
    int getUserStatus(String username);

    Map<String, Object> getUserSignUpInformation(String username);

    int uploadUserSignUpInformation(SignUp signUp);

    int updateUserSignUpInformation(SignUp signUp);

    int deleteUserInformation(String username);

    int getSignUpPersonNumber();

    List<Map<String, Object>> getAllSignUpPerson();

    List<Map<String, Object>> getSomeSignUpPerson(int down, int up);

    List<Map<String, Object>> searchSignUpInformation(Map<String, Object> getMap);

    int updateExamineResult(SignUp signUp);

    
}
