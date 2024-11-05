package msa.devmix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.board.Comment;
import msa.devmix.domain.user.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long boardId; //N:1
    private User user; //N:1, UserDto 대신 User 사용
    private String content;
    private LocalDateTime createdAt; //Auditing Fields
    private LocalDateTime lastModifiedAt; //Auditing Fields

    public static CommentDto of(Long boardId, User user, String content) {
        return CommentDto.of(null, boardId, user, content, null, null);
    }

    public static CommentDto of(Long id, Long boardId, User user, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        return new CommentDto(id, boardId, user, content, createdAt, lastModifiedAt);
    }

    public static CommentDto from(Comment comment) {
        return CommentDto.of(
                comment.getId(),
                comment.getBoard().getId(),
                comment.getUser(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getLastModifiedAt()
        );
    }

    public Comment toEntity(Board board, User user) {
        return Comment.of(
                board,
                user,
                content
        );
    }
}
