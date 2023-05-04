package ru.practicum.service.comment;


import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentWithEventUserDto;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {


    void deleteComment(Long commentId);

    void deleteComment(Long userId, Long commentId);

    CommentWithEventUserDto getComment(Long commentId);

    List<CommentWithEventUserDto> getComments(Long eventId);

    List<CommentWithEventUserDto> getUsersComments(Long userId);

    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto);
}
