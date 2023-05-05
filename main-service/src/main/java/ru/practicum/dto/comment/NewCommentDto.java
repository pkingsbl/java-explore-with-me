package ru.practicum.dto.comment;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class NewCommentDto {

    @NotBlank
    @Length(max = 1000)
    private String text;
}
