package com.minimi.user.mail;

import com.minimi.core.RandomUtil;
import com.minimi.domain.user.request.MailCertifyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final PasswordEncoder passwordEncoder;

    public String sendCertifyMail(MailCertifyForm form) throws MessagingException {
        log.info("::::::: sendCertify Mail :::::::");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, form.getEmail());
        String authCode= RandomUtil.generateRandomNumber(6);
        mimeMessage.setSubject("미니미 인증 메일입니다");
        Context context = new Context();
        context.setVariable("authCode",authCode);
        mimeMessage.setText(springTemplateEngine.process("emailCertify",context),"utf-8","html");
        javaMailSender.send(mimeMessage);
        log.info(":::::::: send mail complete :::::::::");
        return passwordEncoder.encode(authCode);
    }


}
