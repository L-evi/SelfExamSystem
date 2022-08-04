package com.rewrite.selfexamsystem.mapper;

import com.rewrite.selfexamsystem.domain.SignUp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Levi
 * @description: 用户报名user_signup的数据库Mapper
 * @since: 2022/7/29 23:15
 * @version: 3.1（Created By Spring Boot）
 */
@Repository
@Mapper
public interface SignUpMapper {
    /**
     * @param username: 用户名
     * @return 返回用户报名状态码
     * @description : 根据用户名查询用户报名状态
     * @author Levi
     * @since 2022/8/4 19:42
     */
    int getUserStatus(String username);

    /**
     * @param username: 用户名
     * @return 返回用户报名信息以及用户个人信息
     * @description : 根据用户名查询用户报名信息以及个人信息（连表查询）
     * @author Levi
     * @since 2022/8/4 19:43
     */
    Map<String, Object> getUserSignUpInformation(String username);

    /**
     * @param signUp: 用户信息
     * @return 返回改变数据库的行数
     * @description : 用户上传报名信息，并返回插入到数据库中的行数
     * @author Levi
     * @since 2022/8/4 19:44
     */
    int uploadUserSignUpInformation(SignUp signUp);

    /**
     * @param signUp: 更新后的用户信息
     * @return 返回已更新的用户信息条数
     * @description : 用户更新用户信息，并返回数据库中更新的条数
     * @author Levi
     * @since 2022/8/4 19:46
     */
    int updateUserSignUpInformation(SignUp signUp);

    /**
     * @param username:用户名
     * @return 返回删除的条数
     * @description : 用户根据用户名删除数据库中的用户报名信息，并返回删除条数
     * @author Levi
     * @since 2022/8/4 19:46
     */
    int deleteUserInformation(String username);

    /**
     * @param :
     * @return 返回报名人数
     * @description : 管理员根据数据库中报名信息条数获取报名人数
     * @author Levi
     * @since 2022/8/4 19:48
     */
    int getSignUpPersonNumber();

    /**
     * @param :
     * @return 返回所有用户的报名信息和个人信息
     * @description : 管理员获取所有用户的报名信息以及个人信息（连表查询）
     * @author Levi
     * @since 2022/8/4 19:49
     */
    List<Map<String, Object>> getAllSignUpPerson();

    /**
     * @param down:数据的下届
     * @param up:数据的下届
     * @return 返回部分用户信息（分页）
     * @description : 管理员根据分页获取用户报名信息以及个人信息（连表查询）
     * @author Levi
     * @since 2022/8/4 19:52
     */
    List<Map<String, Object>> getSomeSignUpPerson(int down, int up);

    /**
     * @param getMap: 筛选搜索的参数
     * @return 返回经过筛选、搜索后的用户信息
     * @description : 管理员根据筛选搜索查询用户信息（连表查询、动态SQL）
     * @author Levi
     * @since 2022/8/4 19:53
     */
    List<Map<String, Object>> searchSignUpInformation(Map<String, Object> getMap);

    /**
     * @param signUp: 用户报名信息
     * @return 返回受到影响的数据条数
     * @description : 管理员审核用户报名信息并更新报名状态到数据库中
     * @author Levi
     * @since 2022/8/4 19:54
     */
    int updateExamineResult(SignUp signUp);


}
