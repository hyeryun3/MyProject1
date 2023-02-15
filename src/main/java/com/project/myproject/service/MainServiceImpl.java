package com.project.myproject.service;

import com.project.myproject.config.AES256;
import com.project.myproject.config.ConfigUtil;
import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;
import com.project.myproject.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

    private final ConfigUtil config;

    private final UserMapper mapper;


    public String loginUser(User user) throws Exception {
        log.info("::::::check():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::check():::::::: user.getPassword() : {}",user.getPassword());

        String res = "FAILED";

        // DB 데이터 꺼내오기
        List<User> userData = mapper.selectUser();

        User deUser = new User();

        // DB 데이터 복호화
        for(User data: userData){
            deUser = deUserInfo(data);
            // 데이터 비교
            if(user.getEmail().equals(deUser.getEmail()) && user.getPassword().equals(deUser.getPassword())){
                res = "SUCCESS";
                break;
            }
        }

        return res;
    }
    @Transactional
    public int joinUser(UserJoin user) throws Exception{
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
        userInfo = enUserInfo(userInfo);

        // DB 저장
        return mapper.insertUser(userInfo);
    }

    public String mailCheck(UserJoin user){
        String email = user.getEmail() + "@" + user.getEmail2();
        int count = 0;
        try {
            email = enString(email);
            count = mapper.checkMail(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(count>0){
            return "O";
        }else{
            return "X";
        }
    }

    public int mailAuth(String mail) throws NoSuchAlgorithmException {

        log.info("mailAuth()..........");

        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.naver.com");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.required","true");
        props.put("mail.smtp.ssl.trust","smtp.naver.com");

        String adminId = config.getAdminId();
        String adminPw = config.getAdminPw();

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

    public User enUserInfo(User user) throws Exception{
        AES256 aes = new AES256();

        User enUser = new User();

        enUser.setName(aes.encrypt(user.getName()));
        enUser.setPassword(aes.encrypt(user.getPassword()));
        enUser.setEmail(aes.encrypt(user.getEmail()));

        return enUser;
    }

    public User deUserInfo(User user) throws Exception{
        AES256 aes = new AES256();

        User deUser = new User();
        deUser.setName(aes.decrypt(user.getName()));
        deUser.setPassword(aes.decrypt(user.getPassword()));
        deUser.setEmail(aes.decrypt(user.getEmail()));

        return deUser;
    }

    public String enString(String text) throws Exception{
        AES256 aes = new AES256();

        return aes.encrypt(text);
    }
}
