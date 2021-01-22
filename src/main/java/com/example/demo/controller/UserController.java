package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserDTO userDTO) throws
            MethodArgumentNotValidException {
        userService.createUser(new User(userDTO.getFirstName(),
                userDTO.getLastName(),userDTO.getPassword(),
                userDTO.getEmail()));
    }
}
