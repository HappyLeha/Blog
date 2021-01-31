package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private int id;

    @NotBlank(message = "title shouldn't be empty")
    private String title;

    @NotBlank(message = "text shouldn't be empty")
    private String text;

    @NotBlank(message = "status shouldn't be empty")
    @Pattern(regexp = "^PUBLIC$|^DRAFT$",
            message = "status should be PUBLIC OR DRAFT")
    private String status;

    private int authorId;
    private Date createdAt;
    private Date updatedAt;
    private List<TagDTO> tags;
}
