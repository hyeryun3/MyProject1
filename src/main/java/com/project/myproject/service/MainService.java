package com.project.myproject.service;

import com.project.myproject.config.AES256;
import com.project.myproject.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

@Service
@Slf4j
public class MainService {

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

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("rabbith3@naver.com", "P4FXRQPBDK82");
            }
        });

//        String receiver = "receiveMail@gmail.com"; // 메일 받을 주소
        String title = "메일 인증번호가 발송되었습니다.";

        SecureRandom rand = SecureRandom.getInstanceStrong();
        int authNo = rand.nextInt(100000000)+(1000000000-1);
        log.info("authNo.........................................{}, authNo",authNo);


        String content = "<h2>인증번호는 " + authNo + "입니다.</h2>";
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("rabbith3@naver.com", "관리자", "utf-8"));
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
