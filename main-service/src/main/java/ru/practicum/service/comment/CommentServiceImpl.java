package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.entity.Comment;
import ru.practicum.entity.Event;
import ru.practicum.entity.User;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CommentMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    public CommentFullDto getComment(Long commentId) {
        log.info("Public. Get comment id: {}", commentId);

        Comment comment = findComment(commentId);

        return toCommentFullDto(comment);
    }

    @Override
    public List<CommentFullDto> getComments(Long eventId) {
        log.info("Public. Get comments for event id: {}", eventId);

        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentFullDto> getUsersComments(Long userId) {
        log.info("User id {}. Get comments", userId);

        List<Comment> comments = commentRepository.findAllByAuthorId(userId);

        return comments.stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        log.info("User id {}. Add comment for event id: {}", userId, eventId);

        User author = findUser(userId);
        Event event = findEvent(eventId);
        Comment comment = toComment(author, event, newCommentDto, false);
        comment = commentRepository.saveAndFlush(comment);

        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        log.info("User id {}. Update comment id: {}", userId, commentId);

        findUser(userId);
        Comment comment = findComment(commentId);
        checkAuthor(userId, comment);

        comment.setText(newCommentDto.getText());
        comment.setEdited(true);
        comment = commentRepository.saveAndFlush(comment);

        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Admin. Delete comment id: {}", commentId);

        Comment comment = findComment(commentId);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        log.info("User id {}. Delete comment id: {}", userId, commentId);

        findUser(userId);
        Comment comment = findComment(commentId);
        checkAuthor(userId, comment);
        commentRepository.delete(comment);
    }

    private static void checkAuthor(Long userId, Comment comment) {
        if (!Objects.equals(userId, comment.getAuthor().getId())) {
            throw new ForbiddenException("Вы можете редактировать только свои комментарии");
        }
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

}
