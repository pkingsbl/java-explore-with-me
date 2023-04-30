package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentsPublicController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentFullDto>> getComments(@RequestParam Long eventId) {
        List<CommentFullDto> commentFullDtos = commentService.getComments(eventId);
        return new ResponseEntity<>(commentFullDtos, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentFullDto> getComment(@PathVariable Long commentId) {
        CommentFullDto commentFullDto = commentService.getComment(commentId);
        return new ResponseEntity<>(commentFullDto, HttpStatus.OK);
    }
}
