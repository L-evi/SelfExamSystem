package com.rewrite.selfexamsystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description : 邮件的工具类
 * @since 2022/7/27 16:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailUtil {
    /**
     * 发送人
     */
    private String sender;

    /**
     * 收件人
     */
    private String[] recipients;

    /**
     * 主题（标题）
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送邮件的工具
     */
//    private JavaMailSender mailSender = new JavaMailSenderImpl();

    /**
     * @param mailSender 邮件发送工具
     * @return 根据发件人、收件人、发送邮箱的内容发送邮件
     * @description :
     * @author Levi
     * @since 2022/7/27 17:18
     */
    public Boolean sendTextMail(JavaMailSender mailSender) {
//        邮件主体
        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        发件人
        mailMessage.setFrom(sender);
//        发送的标题
        mailMessage.setSubject(subject);
//        发送的内容
        mailMessage.setText(content);
//        根据发件人列表发送邮件
        for (String recipient : recipients) {
            try {
                mailMessage.setTo(recipient);
                mailSender.send(mailMessage);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * @param filespath  需要发送的附件
     * @param mailSender 邮件发送工具
     * @return 返回发送的结果，true：成功，false：失败
     * @description : 将参数中的files作为附件发送邮件
     * @author Levi
     * @since 2022/7/27 18:05
     */
    public Boolean sendFileMail(String[] filespath, JavaMailSender mailSender) throws MessagingException {
//        邮件主体
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//        发件人
        mimeMessageHelper.setFrom(sender);
//        发送的主题（标题）
        mimeMessageHelper.setSubject(subject);
//        发送的主要内容
        mimeMessageHelper.setText(content);
//        添加附加文件
        for (String filepath : filespath) {
            FileSystemResource fileSystemResource = new FileSystemResource(filepath);
            if (!fileSystemResource.exists()) {
                return false;
            }
//            添加附件
            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        }
//        发送
        for (String recipient : recipients) {
            try {
                mimeMessageHelper.setTo(recipient);
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
