package ru.practicum.builder;

import org.springframework.http.HttpStatus;
import ru.practicum.dto.error.ApiError;

public interface ErrorBuilder {
    ApiError build(HttpStatus status, String reason, Exception exception);
}
