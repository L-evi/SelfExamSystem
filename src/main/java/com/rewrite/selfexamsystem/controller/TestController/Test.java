package com.rewrite.selfexamsystem.controller.TestController;

import com.rewrite.selfexamsystem.Annotation.TestAnnotation;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Levi
 * @Date 2022/5/7 11:35
 * @Version 3.0 (created by Spring Boot)
 */

@RestController
public class Test {

    /**
     * @param : null
     * @return Map<String, Object>: 以Map返回提示信息到前端
     * @author Levi
     * @date 2022/5/7 11:33
     */
    // 测试接口：GET请求，请求参数为空，返回字符串Hello World
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Map<String, Object> testHelloWorld() {
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "HelloWorld!");
        return res;
    }

    @TestAnnotation()
    @RequestMapping(value = "/TestPost", method = RequestMethod.POST)
    public Map<String, Object> testPostHelloWorld(@RequestBody Map<String, Object> getMap) {
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Post Hello World!");
        return res;
    }

    /**
     * @param : null
     * @return 返回一个字符串
     * @description : 用于测试AOP切面编程
     * @author Levi
     * @since 2022/7/12 2:59
     */
    @RequestMapping("/helloaop")
    public String SayHello() {
        System.out.println("Hello AOP!");
        return "Hello AOP!";
    }

    @RequestMapping("/helloaop1")
    public String SayHello1() {
        System.out.println("Hello AOP!!!");
        return "1";
    }

    @RequestMapping(value = "/test/getVerifyCode")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        String randomText = KaptchaUtil.getRandomText(200);
        try {
            System.out.println(randomText);
            BufferedImage verifyImage = KaptchaUtil.getVerifyImage(2000, 690, randomText);
            response.setContentType("image/jpeg");
            response.setHeader("key", randomText);
            // 将图片转换陈字符串给前端
            OutputStream stream = response.getOutputStream();
            ImageIO.write(verifyImage, "jpg", stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
