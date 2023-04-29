package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.entity.Event;
import ru.practicum.entity.User;

@Data
@Builder
public class CommentFullDto {

    private Long id;

    private String text;

    private String created;

    private Event event;

    private User author;

    private boolean edited;

}
