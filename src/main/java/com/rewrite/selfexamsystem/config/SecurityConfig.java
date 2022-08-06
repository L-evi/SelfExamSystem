package com.rewrite.selfexamsystem.config;


import com.rewrite.selfexamsystem.filter.JwtAuthenticationTokenFilter;
import com.rewrite.selfexamsystem.handler.AccessDeniedHandler;
import com.rewrite.selfexamsystem.handler.AuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: Levi
 * @description: security的配置类
 * @since: 2022/7/20 10:09
 * @version: 3.1（Created By Spring Boot）
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    Spring security使用PasswordEncoder进行校验，否则数据库中的密码明文存储不安全
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    自定义失败处理器
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    //    添加Token过滤器在账号密码认证之前
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    //      将登陆页面移出需要认证的页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                关闭csrf
                .csrf().disable()
//                不通过session获取SecurityContext（前后端分离），SecurityContext存储认证之后的用户信息
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
//                对于登录接口允许匿名访问：即未认证也可以访问
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/register").anonymous()
                .antMatchers("/admin/login").anonymous()
                .antMatchers("/getVerifyCode").anonymous()
                .antMatchers("/checkVerifyCode").anonymous()
                .antMatchers("/user/forgetPassword").anonymous()
                .antMatchers("/user/resetPassword").anonymous()
                .antMatchers("/helloaop").anonymous()
//                除了以上的页面，全部都需要认证
                .anyRequest().authenticated();

//        把token的过滤放入到账号密码过滤之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        配置自定义失败处理器
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler).and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }


    //    重写这个方法用于用户认证，不用走默认的登录认证页面
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
