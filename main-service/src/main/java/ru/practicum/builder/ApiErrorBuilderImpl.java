package ru.practicum.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.dto.error.ApiError;

import java.util.List;

@Slf4j
@Component
public class ApiErrorBuilderImpl implements ErrorBuilder {
    @Override
    public ApiError build(HttpStatus status, String reason, Exception exception) {
        log.info("Start build error response with status: {}, reason {}", status.name(), status.getReasonPhrase());

        return ApiError.builder()
                .errors(List.of(exception.getClass().getName()))
                .status(status.name())
                .reason(reason)
                .message(exception.getLocalizedMessage())
                .build();
    }
}
