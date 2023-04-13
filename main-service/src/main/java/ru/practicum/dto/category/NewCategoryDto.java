package ru.practicum.dto.category;

import javax.validation.constraints.NotBlank;

public class NewCategoryDto {

    @NotBlank
    private String name;

}
