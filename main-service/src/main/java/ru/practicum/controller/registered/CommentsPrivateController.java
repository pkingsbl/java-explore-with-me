package ru.practicum.controller.registered;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentsPrivateController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentFullDto>> getComment(@PathVariable Long userId) {
        List<CommentFullDto> commentFullDtos = commentService.getUsersComments(userId);
        return new ResponseEntity<>(commentFullDtos, HttpStatus.OK);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody NewCommentDto newCommentDto
    ) {
        CommentDto commentDto = commentService.addComment(userId, eventId, newCommentDto);
        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody NewCommentDto newCommentDto
    ) {
        CommentDto commentDto = commentService.updateComment(userId, commentId, newCommentDto);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long userId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
