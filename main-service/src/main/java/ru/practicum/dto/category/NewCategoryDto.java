package ru.practicum.dto.category;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class NewCategoryDto {

    @NotBlank
    @Length(max = 255)
    private String name;

}
