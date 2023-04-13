package ru.practicum.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class NewUserRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

}
