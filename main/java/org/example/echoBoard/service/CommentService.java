package org.example.echoBoard.service;

import lombok.RequiredArgsConstructor;
import org.example.echoBoard.dto.response.CommentResponse;
import org.example.echoBoard.model.Comment;
import org.example.echoBoard.model.Notification;
import org.example.echoBoard.model.Post;
import org.example.echoBoard.model.User;
import org.example.echoBoard.repository.CommentRepository;
import org.example.echoBoard.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final UserService userService;

    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void addComment(Long postId, Long userId, String content) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

    }

    @Transactional
    public  CommentResponse addComment(Long postId, Long userId, String content, Long parentId) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);
        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글 없음"));
        }

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .parent(parent) // 🔥 null이면 댓글, 있으면 대댓글
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        // 알림 생성
        if (!post.getUser().getId().equals(userId)) {
            int limit = 20;
            String preview;
            if (content.length() > limit) {
                int cut = content.lastIndexOf(" ", limit);
                if (cut == -1) {
                    cut = limit;
                }
                preview = content.substring(0, cut) + "…";
            } else {
                preview = content;
            }
            String message = user.getUsername()
                    + "님이 댓글을 남겼습니다: \""
                    + preview + "\"";

            String url = "/posts/" + post.getId();

            Notification notification = new Notification(
                    post.getUser(),
                    message,
                    url
            );
            notificationRepository.save(notification);
        }

        return toDto(comment);

    }

    public void addReply(Long postId, Long parentId, Long userId, String content) {
        Post post = postService.findById(postId);
        User user = userService.findById(userId);
        Comment parent = commentRepository.findById(parentId).orElseThrow();

        Comment reply = Comment.builder()
                .post(post)
                .user(user)
                .parent(parent)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(reply);
    }

    public List<CommentResponse> findByPostId(Long postId) {

        List<Comment> parents =
                commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);

        return parents.stream().map(parent -> {
            CommentResponse parentDto = toDto(parent);

            List<CommentResponse> replies =
                    commentRepository.findByParentIdOrderByCreatedAtAsc(parent.getId())
                            .stream()
                            .map(this::toDto)
                            .filter(Objects::nonNull)
                            .toList();

            parentDto.setReplies(replies);
            return parentDto;
        })
                .toList();
    }

    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        commentRepository.deleteRepliesByPostId(postId);
        commentRepository.deleteParentCommentsByPostId(postId);
    }


    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("댓글 없음");
        }

        // 대댓글 먼저 삭제
        commentRepository.deleteRepliesByParentId(commentId);

        // 부모 댓글 삭제
        commentRepository.deleteByIdDirect(commentId);
    }


    private CommentResponse toDto(Comment comment) {
        try {
            return CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .username(comment.getUser().getUsername())
                    .userId(comment.getUser().getId())
                    .createdAt(comment.getCreatedAt())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .replies(new ArrayList<>())
                    .build();
        } catch (Exception e) {
            System.out.println("댓글 DTO 변환 실패 id={}"+ comment.getId() +e);
            return null;
        }
    }

}
