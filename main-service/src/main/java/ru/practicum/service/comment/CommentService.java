package ru.practicum.service.comment;


import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {


    void deleteComment(Long commentId);

    void deleteComment(Long userId, Long commentId);

    CommentFullDto getComment(Long commentId);

    List<CommentFullDto> getComments(Long eventId);

    List<CommentFullDto> getUsersComments(Long userId);

    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto);
}
