package ru.practicum.dto.user;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class NewUserRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

}
