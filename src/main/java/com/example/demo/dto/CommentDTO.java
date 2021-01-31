package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private int id;

    @NotBlank(message = "message shouldn't be empty")
    private String message;

    private Date createdAt;
    private int postId;
    private int authorId;
}
