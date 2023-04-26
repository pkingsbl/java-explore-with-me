package ru.practicum.mapper;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.entity.Comment;
import ru.practicum.entity.Event;
import ru.practicum.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated().format(DATE_TIME_FORMATTER))
                .event(comment.getEvent())
                .author(comment.getAuthor())
                .build();
    }


    public static Comment toComment(User author, Event event, NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated().format(DATE_TIME_FORMATTER))
                .authorName(comment.getAuthor().getName())
                .build();
    }
}
