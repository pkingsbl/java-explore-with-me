package ru.practicum.service.user;


import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto registerUser(NewUserRequest newUserRequest);

    void delete(Long userId);

}
