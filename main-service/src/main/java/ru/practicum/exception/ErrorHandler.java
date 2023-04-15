package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.builder.ApiErrorBuilderImpl;
import ru.practicum.builder.ErrorBuilder;
import ru.practicum.dto.error.ApiError;

import javax.validation.ConstraintViolationException;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final ErrorBuilder errorBuilder = new ApiErrorBuilderImpl();
    private final String reasonConflict = "Integrity constraint has been violated.";
    private final String reasonBadRq = "Incorrectly made request.";
    private final String reasonNotFound = "The required object was not found.";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(final ConstraintViolationException e) {
        log.warn("Не удалось выполнить запись в базу данных: " + e.getMessage());

        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, reasonConflict, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleValidation(final DataIntegrityViolationException e) {
        log.warn("Не удалось выполнить запись в базу данных: " + e.getMessage());

        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, reasonConflict, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleValidation(final NotFoundException e) {
        log.warn("Не удалось найти запись в базе данных: " + e.getMessage());

        ApiError errorRs = errorBuilder.build(HttpStatus.NOT_FOUND, reasonNotFound, e);
        return new ResponseEntity<>(errorRs, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            ValidationException.class
    })
    public ResponseEntity<ApiError> handleValidation(final Exception e) {
        log.warn("Неправильно составленный запрос: " + e.getMessage());

        ApiError errorRs = errorBuilder.build(HttpStatus.BAD_REQUEST, reasonBadRq,  e);
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }

}