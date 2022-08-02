package com.rewrite.selfexamsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author: Levi
 * @description: 继承UserDetails的实体类，可以用于SpringSecurity中认证
 * @since: 2022/7/19 22:04
 * @version: 3.1（Created By Spring Boot）
 */
//使用注解setter、getter、toString等方法以及有参、无参构造方法全部补全
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData implements UserDetails {
    private String username;
    private String password;

    /**
     * @return 返回权限信息
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * @return 返回用户密码
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * @return 返回用户名
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * @return 返回是否认证过期：true代表没过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 返回账户是否锁定：true代表未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 返回证书是否已过期：true代表未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 返回账户是否可用：true代表可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
