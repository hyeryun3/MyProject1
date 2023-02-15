package com.project.myproject.service;

import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;

import java.security.NoSuchAlgorithmException;

public interface MainService {

    User enUserInfo(User user) throws Exception;

    User deUserInfo(User user) throws Exception;

    String loginUser(User user) throws Exception;

    int joinUser(UserJoin user) throws Exception;

    int mailAuth(String mail) throws NoSuchAlgorithmException;

    String mailCheck(UserJoin user);

}
