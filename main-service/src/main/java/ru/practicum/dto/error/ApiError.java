package ru.practicum.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ApiError {

    private List<String> errors;

    private String message;

    private String reason;

    private String status;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
}
