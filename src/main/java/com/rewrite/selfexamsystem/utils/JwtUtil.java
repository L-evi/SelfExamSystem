package com.rewrite.selfexamsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import lombok.val;


import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author: Levi
 * @description: JWT的工具类
 * @since: 2022/7/19 14:15
 * @version: 3.1（Created By Spring Boot）
 */
@Getter
public class JwtUtil {
    //    有效期：三个小时，毫秒为单位
    private static Long JWT_TTL = 60 * 60 * 1000 * 3L;

    //    JWT的密钥key，待会要进行base64编码
    private static String JWT_KEY = "SelfExamSystem";

    //    JWT的发行者：计算机学院
    private static String JWT_SUBJECT = "ComputingSchool";


    /**
     * @param :
     * @return 返回编码加密后的key
     * @description: 通过Base64编码和AES加密后返回key
     * @author Levi
     * @since 2022/7/19 15:37
     */
    private static SecretKey getSecretKey() {
//        使用Base64编码
        byte[] decode = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(decode, 0, decode.length, "AES");
        return key;
    }

    /**
     * @param :
     * @return 返回uuid
     * @description: 利用UUID实现类生成uuid
     * @author Levi
     * @since 2022/7/19 15:40
     */
    private static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * @param Object: 主体：可以为JSON数据
     * @param ttl:    过期时间
     * @param uuid:   唯一的ID
     * @return 返回JwtBuilder
     * @description: 将uuid、主体、签发时间、签名类型、密钥、过期时间加入到JWT中
     * @author Levi
     * @since 2022/7/19 16:48
     */
    private static JwtBuilder getJwtBuilder(String Object, Long ttl, String uuid) {
        //        设置加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        获取加密密钥
        SecretKey secretKey = getSecretKey();
//        获取过期时间
        Long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        if (ttl == null) {
            ttl = JWT_TTL;
        }
        Long endMills = nowMills + ttl;
        Date end = new Date(endMills);
        return Jwts.builder()
                .setId(uuid)        //唯一的id：uuid
                .setSubject(Object) //主体：JSON数据
                .setIssuer(JWT_SUBJECT) //签发者
                .setIssuedAt(now)       //签发时间
                .signWith(signatureAlgorithm, secretKey)  //设置签名类型以及密钥
                .setExpiration(end);  //设置过期时间
    }

    //    加密生成token
    //    有参构造函数
    public static String createJwt(String subject) {
        return getJwtBuilder(subject, null, getUUID()).compact();
    }

    public static String createJwt(String subject, Long ttl) {
        return getJwtBuilder(subject, ttl, getUUID()).compact();
    }

    public static String createJwt(String subject, Long ttl, String uuid) {
        return getJwtBuilder(subject, ttl, uuid).compact();
    }

    //    解析token
    public static Claims parseJwt(String token) throws Exception {
        SecretKey secretKey = getSecretKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}

