package com.project.myproject.repository;

import com.project.myproject.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    List<User> selectUser();

    int insertUser(User user);

    int checkMail(String mail);
}
