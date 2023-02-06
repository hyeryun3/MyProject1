package com.project.myproject.controller;

import com.project.myproject.config.AES256;
import com.project.myproject.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/home")
    public String home(){
        log.info("::::::home()::::::");
        return "home";
    }

    @PostMapping("/check")
    public ResponseEntity check(@RequestBody User user) throws Exception {
        log.info("::::::check():::::::: user.getId() : {}",user.getId());
        log.info("::::::check():::::::: user.getPw() : {}",user.getPw());
        AES256 aes = new AES256();
        String secretData = aes.encrypt(user.getId());
        log.info("::::::encrypt Data : {}",secretData);
        log.info("::::::decrypt Data : {}", aes.decrypt(secretData));
        return ResponseEntity.ok(user);
    }


}

