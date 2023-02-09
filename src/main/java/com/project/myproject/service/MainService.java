package com.project.myproject.service;

import com.project.myproject.config.AES256;
import com.project.myproject.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("/admin.properties")
public class MainService {

    private final Environment env;

    public User aesProc(User user) throws Exception{
        AES256 aes = new AES256();
        String secretName = aes.encrypt(user.getName());
        String secretPw = aes.encrypt(user.getPassword());

        User userAes = user;
        userAes.setName(secretName);
        userAes.setPassword(secretPw);

        return userAes;
    }


    public int auth(String mail) throws NoSuchAlgorithmException {

        log.info("auth()..........");

        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.naver.com");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.required","true");
        props.put("mail.smtp.ssl.trust","smtp.naver.com");

        String adminId = env.getProperty("adminId");
        String adminPw = env.getProperty("adminPw");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminId, adminPw);
            }
        });

        String title = "메일 인증번호가 발송되었습니다.";

        SecureRandom rand = SecureRandom.getInstanceStrong();
        int authNo = rand.nextInt(100000000)+(1000000000-1);
        log.info("authNo.........................................{}, authNo",authNo);


        String content = "<h2>인증번호는 " + authNo + "입니다.</h2>";
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(adminId, "개발관리자", "utf-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            message.setSubject(title);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authNo;

    }
}
