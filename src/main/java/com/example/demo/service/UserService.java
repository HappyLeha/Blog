package com.example.demo.service;

import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.entity.User;

import javax.mail.MessagingException;

public interface UserService {
    void createUser(User user);
    void confirmUser(String token);
    void createCode(String email);
    void confirmCode(NewPasswordDTO newPasswordDTO);
}
