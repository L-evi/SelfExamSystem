package com.rewrite.selfexamsystem.utils;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  验证码工具类
 * @since 2022/8/4 20:42
 */
@Configuration
public class KaptchaUtil {
    private static final String CHAR_STR = "123456789abcdefghijklmnopqrsduvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * @param weight: 图片宽度
     * @param height: 图片高度
     * @param length: 验证码长度
     * @return 返回Producer图片生成工具
     * @description : 设置生成验证码的配置
     * @author Levi
     * @since 2022/8/5 9:46
     */
    private static Producer producer(String weight, String height, String length) {
        Properties properties = new Properties();
        //设置图片边框
        properties.setProperty("kaptcha.border", "no");
        //设置图片边框为蓝色
        //properties.setProperty("kaptcha.border.color", "white");
        // 背景颜色渐变开始
        properties.put("kaptcha.background.clear.from", getRandomColor());
        // 背景颜色渐变结束
        properties.put("kaptcha.background.clear.to", getRandomColor());
        // 字体颜色：随机颜色
        properties.put("kaptcha.textproducer.font.color", getRandomColor());
        // 文字间隔
        properties.put("kaptcha.textproducer.char.space", "10");
        // 干扰线颜色配置
        properties.put("kaptcha.noise.color", getRandomColor());
        //如果需要去掉干扰线
        //properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // 字体
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 图片宽度
        properties.setProperty("kaptcha.image.width", weight);
        // 图片高度
        properties.setProperty("kaptcha.image.height", height);
        // 从哪些字符中产生
        properties.setProperty("kaptcha.textproducer.char.string", CHAR_STR);
        // 字符个数
        properties.setProperty("kaptcha.textproducer.char.length", length);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(new Config(properties));
        return defaultKaptcha;
    }

    /**
     * @param :
     * @return RGB颜色的字符串形式
     * @description : 通过随机数函数生成随机RGB颜色
     * @author Levi
     * @since 2022/8/5 9:49
     */
    private static String getRandomColor() {
        Random random = new Random();
        return random.nextInt(256) + "," + random.nextInt(256) + "," + random.nextInt(256);
    }

    /**
     * @param length: 验证码长度
     * @return 返回验证码文本
     * @description : 生成指定长度length的验证码文本
     * @author Levi
     * @since 2022/8/5 9:49
     */
    public static String getRandomText(int length) {
        StringBuilder text = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            text.append(CHAR_STR.charAt(random.nextInt(CHAR_STR.length())));
        }
        return text.toString();
    }

    /**
     * @param weight: 图片宽度
     * @param height: 图片高度
     * @param text:   验证码文本
     * @return 返回BuggeredImage图片
     * @description : 通过producer生成指定长度宽度的验证码图片
     * @author Levi
     * @since 2022/8/5 9:50
     */
    public static BufferedImage getVerifyImage(int weight, int height, String text) {
        Producer kaptchaProducer = producer(String.valueOf(weight), String.valueOf(height), String.valueOf(text.length()));
        return kaptchaProducer.createImage(text);
    }


    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static Map<String, Object> checkVerifyCode(String encode, String verifyCode) {
        Map<String, Object> res = new HashMap<>();
        if (encode == null || encode.isEmpty() || "".equals(encode)) {
            res.put("status", "fail");
            res.put("des", "验证码已过期，请重新获取");
            return res;
        }
//        是否匹配
        if (!bCryptPasswordEncoder.matches(verifyCode, encode)) {
            res.put("status", "fail");
            res.put("des", "验证码错误，请重新输入");
            return res;
        }
        res.put("status", "success");
        res.put("des", "验证成功");
        return res;
    }
}
