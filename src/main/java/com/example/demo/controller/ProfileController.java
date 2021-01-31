package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("profile")
public class ProfileController {
    UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getProfile(Principal principal) {
        User user = userService.getProfile(principal);
        return new UserDTO(user.getId(), user.getFirstName(),
                user.getLastName(), null, user.getEmail(),
                user.getCreatedAt());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateProfile(Principal principal,
                              @RequestBody @Valid UserDTO userDTO) {
        userService.updateProfile(principal, userDTO);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(Principal principal) {
        userService.deleteProfile(principal);
    }
}
