package com.example.haustoj.manager.email;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.UnicodeUtil;
import org.thymeleaf.TemplateEngine;
import com.example.haustoj.config.NacosSwitchConfig;
import com.example.haustoj.config.WebConfig;
import com.example.haustoj.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Properties;

/**
 * @Description
 * @Author ouyu
 * @Date 2025-10-31 13:28
 **/
@Component
@RefreshScope
@Slf4j(topic = "haustoj-email")
public class EmailManager {
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private NacosSwitchConfig nacosSwitchConfig;
    /**
    * @Description  获取邮件系统设置
    * @param * @param
    * @return org.springframework.mail.javamail.JavaMailSenderImpl
    * @Author ouyu
    * @Date 2025/10/31
    */
    private JavaMailSenderImpl getMailSender() {
        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(webConfig.getEmailHost());
        mailSender.setPort(webConfig.getEmailPort());
        mailSender.setUsername(webConfig.getEmailUsername());
        mailSender.setPassword(webConfig.getEmailPassword());
        mailSender.setDefaultEncoding("UTF-8");

        Properties p = new Properties();
        p.setProperty("mail.smtp.ssl.enable", webConfig.getEmailSsl().toString());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.starttls.enable", webConfig.getEmailSsl().toString());
        mailSender.setJavaMailProperties(p);
        return mailSender;
    }
    /**
    * @Description 检查邮件系统是否已经配置完毕
    * @param
    * @return java.lang.Boolean
    * @Author ouyu
    * @Date 2025/10/31
    */
    private Boolean isOk() {
        WebConfig webConfig = nacosSwitchConfig.getWebConfig();
        return webConfig.getEmailHost() != null
                && webConfig.getEmailPort() != null
                && webConfig.getEmailUsername() != null
                && webConfig.getEmailPassword() != null;
    }
    /**
    * @Description 发送注册邮件验证码
    * @param * @param to
    * @param email
    * @param code
    * @return void
    * @Author ouyu
    * @Date 2025/10/31
    */
    public void sendRegisterCode(String email, String code) {
        DateTime expireTime = DateUtil.offsetMinute(new Date(), 10);
        JavaMailSenderImpl mailSender = getMailSender();
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            WebConfig webConfig = nacosSwitchConfig.getWebConfig();
            context.setVariable(Constants.Email.OJ_NAME.getValue(), webConfig.getName());
            context.setVariable(Constants.Email.OJ_SHORT_NAME.getValue(), webConfig.getShortName());
            context.setVariable(Constants.Email.OJ_URL.getValue(), webConfig.getBaseUrl());
            context.setVariable(Constants.Email.EMAIL_BACKGROUND_IMG.getValue(), webConfig.getEmailBGImg());
            context.setVariable("CODE", code);
            context.setVariable("EXPIRE_TIME",expireTime.toString());
            String emailContent = templateEngine.process("emailTemplate_registerCode", context);
            helper.setSubject(UnicodeUtil.toString(webConfig.getShortName() + "的注册邮件"));
            helper.setText(emailContent, true);
            helper.setFrom(webConfig.getEmailUsername());
            helper.setTo(email);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
    }
}
