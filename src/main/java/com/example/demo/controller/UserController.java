package com.example.demo.controller;

import com.example.demo.dto.EmailDTO;
import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserDTO userDTO) {
        userService.createUser(new User(userDTO.getFirstName(),
                userDTO.getLastName(),userDTO.getPassword(),
                userDTO.getEmail()));
    }

    @GetMapping("/confirm/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void confirmUser(@PathVariable("token") String token) {
        userService.confirmUser(token);
    }

    @PostMapping("/forgot_password")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCode(@RequestBody @Valid EmailDTO emailDTO) {
        userService.createCode(emailDTO.getEmail());
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCode(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {
        userService.confirmCode(newPasswordDTO);
    }
}
