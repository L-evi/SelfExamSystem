package com.rewrite.selfexamsystem.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Levi
 * @description: 将Http的对象渲染到客户端的工具类
 * @since: 2022/7/21 2:23
 * @version: 3.1（Created By Spring Boot）
 */

public class WebUtil {
    /**
     * @param response: 渲染对象
     * @param string:   返回的字符串，实际上已经被序列化为json
     * @return 返回一个字符串，默认为null
     * @description: 用于security认证或者鉴权失败时将失败结果渲染到前段的工具类
     * @author Levi
     * @since 2022/7/21 2:26
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
