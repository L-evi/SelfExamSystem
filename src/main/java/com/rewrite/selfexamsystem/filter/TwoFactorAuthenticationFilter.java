package com.rewrite.selfexamsystem.filter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

public class TwoFactorAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = request.getParameter(getUsernameParameter());
        String extraInput = request.getParameter("role");
        String combinedUsername = username + ":" + extraInput;
        System.out.println(combinedUsername);
        return combinedUsername;
    }
}