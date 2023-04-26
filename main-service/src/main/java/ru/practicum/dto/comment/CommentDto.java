package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {

    private Long id;

    private String text;

    private String created;

    private String authorName;

}
