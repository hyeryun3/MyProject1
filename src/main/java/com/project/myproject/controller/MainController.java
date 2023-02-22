package com.project.myproject.controller;

import com.project.myproject.config.JwtTokenUtil;
import com.project.myproject.model.Email;
import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;
import com.project.myproject.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final MainService service;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${secret.key}")
    private String secretKey;

    @PostMapping("/mailCheck")
    public ResponseEntity mailCheck(@RequestBody UserJoin user){
        String res = service.mailCheck(user);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/create/token")
    public ResponseEntity createToken(HttpServletRequest req, HttpServletResponse res, User user) throws Exception{
        String key = req.getHeader("key");

//        key = Base64.getEncoder().encodeToString(key.getBytes());

        String token = "";
        // key값이 같으면 토큰 발행.
        if(secretKey.equals(key)){
            token = jwtTokenUtil.createToken(key,user.getEmail());
            res.setHeader("Authorization", token);

            Map<String, Object> map = jwtTokenUtil.verifyJWT(key, token);
            System.out.println(map);

        }else{
//            res.setHeader("token", token);
        }

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody HashMap<String, String> map, HttpServletResponse response) throws Exception{

        log.info("::::::login()::::::");
        log.info("::::::login():::::::: user.getEmail() : {}",map.get("email"));
        log.info("::::::login():::::::: user.getPassword() : {}",map.get("password"));

//        if(res.equals("SUCCESS")){


//        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody UserJoin user) throws Exception {
        log.info("::::::join():::::::: user.getName() : {}",user.getName());
        log.info("::::::join():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::join():::::::: user.getEmail2() : {}",user.getEmail2());
        log.info("::::::join():::::::: user.getPassword() : {}",user.getPassword());

        int res = service.joinUser(user);
        if(res > 0){
            return ResponseEntity.ok("SUCCESS");
        }else{
            return ResponseEntity.ok("FAILED");
        }
    }

    @PostMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestBody Email email) throws Exception {
        log.info("::::::checkEmail():::::::: user.getEmail() : {}", email.getEmail());
        log.info("::::::checkEmail():::::::: user.getEmail2() : {}", email.getEmail2());

        String mail = email.getEmail() + "@" + email.getEmail2();
        int authNo = service.mailAuth(mail);

        return ResponseEntity.ok(authNo);
    }

}

