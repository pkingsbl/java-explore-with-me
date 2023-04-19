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
    private static final String REASON_CONFLICT = "Integrity constraint has been violated.";
    private static final String REASON_BAD_RQ = "Incorrectly made request.";
    private static final String REASON_NOT_FOUND = "The required object was not found.";
    private static final String REASON_FORBIDDEN = "For the requested operation the conditions are not met.";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(final ConstraintViolationException e) {
        log.warn("Не удалось выполнить запись в базу данных: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, REASON_CONFLICT, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleValidation(final DataIntegrityViolationException e) {
        log.warn("Не удалось выполнить запись в базу данных: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, REASON_CONFLICT, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleValidation(final ConflictException e) {
        log.warn("Недопустиммое занчение: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, REASON_CONFLICT, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleValidation(final HttpMessageNotReadableException e) {
        log.warn("Недопустиммое занчение: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.CONFLICT, REASON_CONFLICT, e);
        return new ResponseEntity<>(errorRs, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleValidation(final NotFoundException e) {
        log.warn("Не удалось найти запись в базе данных: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.NOT_FOUND, REASON_NOT_FOUND, e);
        return new ResponseEntity<>(errorRs, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleValidation(final ForbiddenException e) {
        log.warn("Недопустиммое занчение: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.FORBIDDEN, REASON_FORBIDDEN, e);
        return new ResponseEntity<>(errorRs, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ValidationException.class
    })
    public ResponseEntity<ApiError> handleValidation(final Exception e) {
        log.warn("Неправильно составленный запрос: {}", e.getMessage());
        ApiError errorRs = errorBuilder.build(HttpStatus.BAD_REQUEST, REASON_BAD_RQ,  e);
        return new ResponseEntity<>(errorRs, HttpStatus.BAD_REQUEST);
    }

}