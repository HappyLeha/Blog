package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    @NotBlank(message = "firstName shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+",
            message = "firstName should includes only letters")
    private String firstName;
    @NotBlank(message = "lastName shouldn't be empty")
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+",
            message = "lastName should includes only letters")
    private String lastName;
    @NotBlank(message = "password shouldn't be empty")
    private String password;
    @NotBlank(message = "email shouldn't be empty")
    @Email(message = "field email should be email")
    private String email;
    private Date createdAt;
}
