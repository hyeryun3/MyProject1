package com.project.myproject.controller;

import com.project.myproject.model.Email;
import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;
import com.project.myproject.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
    private final MainService service;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) throws Exception{
        log.info("::::::login()::::::");
        log.info("::::::login():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::login():::::::: user.getPassword() : {}",user.getPassword());

        String res = service.loginUser(user);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody UserJoin user) throws Exception {
        log.info("::::::join():::::::: user.getName() : {}",user.getName());
        log.info("::::::join():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::join():::::::: user.getEmail2() : {}",user.getEmail2());
        log.info("::::::join():::::::: user.getPassword() : {}",user.getPassword());

        service.joinUser(user);

        return ResponseEntity.ok(user);
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

