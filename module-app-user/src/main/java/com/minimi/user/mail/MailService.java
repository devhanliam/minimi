package com.minimi.user.mail;

import com.minimi.core.RandomUtil;
import com.minimi.domain.user.request.MailCertifyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final PasswordEncoder passwordEncoder;
    private final RandomUtil randomUtil;
    public String sendCertifyMail(MailCertifyForm form) throws MessagingException {
        log.info("::::::: sendCertify Mail start:::::::");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, form.getEmail());
        String authCode= randomUtil.generateRandomNumber(6);
        mimeMessage.setSubject("미니미 인증 메일입니다");
        Context context = new Context();
        context.setVariable("authCode",authCode);
        mimeMessage.setText(springTemplateEngine.process("emailCertify",context),"utf-8","html");
        javaMailSender.send(mimeMessage);
        stopWatch.stop();
        log.info(":::::::: send mail complete {}ms:::::::::",stopWatch.getTotalTimeMillis());
        return passwordEncoder.encode(authCode);
    }


}
