package com.project.myproject.service;

import com.project.myproject.config.AES256;
import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;
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
public class MainServiceImpl implements MainService{

    private final Environment env;

    public User enUserInfo(User user) throws Exception{
        AES256 aes = new AES256();
        aes.encrypt(user.getName());
        aes.encrypt(user.getPassword());
        aes.encrypt(user.getEmail());

        return user;
    }

    public User deUserInfo(User user) throws Exception{
        AES256 aes = new AES256();
        aes.decrypt(user.getName());
        aes.decrypt(user.getPassword());
        aes.decrypt(user.getEmail());

        return user;
    }

    public String loginUser(User user) throws Exception {
        log.info("::::::check():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::check():::::::: user.getPassword() : {}",user.getPassword());

        // DB 데이터 꺼내오기
        User userData = new User();

        // DB 데이터 복호화
        userData = deUserInfo(userData);

        // 데이터 비교
        if(user.getEmail().equals(userData.getEmail()) && user.getPassword().equals(userData.getPassword())){
            return "success";
        }else{
            return "failed";
        }
    }

    public void joinUser(UserJoin user) throws Exception{
        log.info("::::::check():::::::: user.getName() : {}",user.getName());
        log.info("::::::check():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::check():::::::: user.getEmail2() : {}",user.getEmail2());
        log.info("::::::check():::::::: user.getPassword() : {}",user.getPassword());

        User userInfo = new User();

        String email = user.getEmail() + "@" + user.getEmail2();

        userInfo.setName(user.getName());
        userInfo.setPassword(user.getPassword());
        userInfo.setEmail(email);
        // 이름, 비밀번호, 메일 암호화
        enUserInfo(userInfo);

        // DB 저장
    }


    public int mailAuth(String mail) throws NoSuchAlgorithmException {

        log.info("mailAuth()..........");

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
