package com.example.demo.service;

import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.security.Principal;

public interface UserService extends UserDetailsService {

    void createUser(User user);

    void confirmUser(String token);

    void createCode(String email);

    void confirmCode(NewPasswordDTO newPasswordDTO);

    User getByEmailAndPassword(String email, String password);

    User getProfile(Principal principal);

    void updateProfile(Principal principal, UserDTO userDTO);

    void deleteProfile(Principal principal);
}
