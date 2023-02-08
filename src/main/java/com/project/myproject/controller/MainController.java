package com.project.myproject.controller;

import com.project.myproject.config.AES256;
import com.project.myproject.model.Email;
import com.project.myproject.model.User;
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

    @GetMapping("/home")
    public String home(){
        log.info("::::::home()::::::");
        return "home";
    }

    @PostMapping("/check")
    public ResponseEntity check(@RequestBody User user) throws Exception {
        log.info("::::::check():::::::: user.getId() : {}",user.getName());
        log.info("::::::check():::::::: user.getEmail() : {}",user.getEmail());
        log.info("::::::check():::::::: user.getEmail2() : {}",user.getEmail2());
        log.info("::::::check():::::::: user.getPassword() : {}",user.getPassword());

        User userAes = service.aesProc(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity checkEmail(@RequestBody Email email) throws Exception {
        log.info("::::::check():::::::: user.getEmail() : {}", email.getEmail());
        log.info("::::::check():::::::: user.getEmail2() : {}", email.getEmail2());

        String mail = email.getEmail() + "@" + email.getEmail2();
        int authNo = service.auth(mail);

        return ResponseEntity.ok(authNo);
    }



}

