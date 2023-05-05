package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentWithEventUserDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentsPublicController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentWithEventUserDto>> getComments(@RequestParam Long eventId) {
        List<CommentWithEventUserDto> commentWithEventUserDtos = commentService.getComments(eventId);
        return new ResponseEntity<>(commentWithEventUserDtos, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentWithEventUserDto> getComment(@PathVariable Long commentId) {
        CommentWithEventUserDto commentWithEventUserDto = commentService.getComment(commentId);
        return new ResponseEntity<>(commentWithEventUserDto, HttpStatus.OK);
    }
}
