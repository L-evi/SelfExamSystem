package com.rewrite.selfexamsystem.mapper;
// 获取数据库连接

import com.rewrite.selfexamsystem.domain.UserInformation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Levi
 * @Date 2022/5/7 11:40
 * @Version 3.0 (created by Spring Boot)
 */

//加入bean管理，能够自动装配
@Repository
//加入Mapper数据库SQL映射才能被容器识别
@Mapper
public interface UserInformationMapper {
    /**
     * @param username: 传入的准考证号，用于搜索
     * @return UserInformation: 将搜索到的数据存入User结构体中，然后返回
     * @author Levi
     * @description : 对数据库使用搜索功能：对传入的username（准考证号）进行搜索，将搜寻的对象返回到前一层，如果没有的话，将返回空
     * @since 2022/5/7 11:29
     */
    UserInformation selectByUsername(String username);

    /**
     * @param userInformation: 通过User将一个用户的数据存入数据库
     * @return void
     * @author Levi
     * @description : 添加用户数据：不返回主键
     * @since 2022/5/7 11:29
     */
    void addUser(UserInformation userInformation);

    Boolean updateUserInformation(UserInformation userInformation);

    /**
     * @param :
     * @return 返回所有用户的邮箱
     * @description : 获取所有用户的邮箱
     * @author Levi
     * @since 2022/7/27 23:21
     */
    List<String> getAllUserEmail();

    /**
     * @param username:用户名
     * @return 邮箱
     * @description : 获取用户邮箱：通过username获取用户邮箱
     * @author Levi
     * @since 2022/8/5 16:08
     */
    String getEmail(String username);
}
