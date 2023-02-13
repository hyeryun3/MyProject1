package com.project.myproject.service;

import com.project.myproject.model.User;
import com.project.myproject.model.UserJoin;

import java.security.NoSuchAlgorithmException;

public interface MainService {

    public User enUserInfo(User user) throws Exception;

    public User deUserInfo(User user) throws Exception;

    public String loginUser(User user) throws Exception;

    public void joinUser(UserJoin user) throws Exception;

    public int mailAuth(String mail) throws NoSuchAlgorithmException;

}
