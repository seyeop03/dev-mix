package msa.devmix.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.dto.CommentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentsResponse {

    private Long commentId;
    private Long boardId;//N:1
    private String boardTitle;
    private String userNickName;
    private String content;
    private String createdAt; //Auditing Fields
    private LocalDateTime lastModifiedAt; //Auditing Fields

    public static CommentsResponse of(Long commentId,
                                      Long boardId,
                                      String boardTitle,
                                      String userNickName,
                                      String content,
                                      String createdAt,
                                      LocalDateTime lastModifiedAt) {
        return new CommentsResponse(commentId, boardId, boardTitle, userNickName, content, createdAt, lastModifiedAt);
    }

    public static CommentsResponse from(CommentDto commentDto) {
        String createdAtString = commentDto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return CommentsResponse.of(
                commentDto.getCommentId(),
                commentDto.getBoardId(),
                commentDto.getBoardTitle(),
                commentDto.getUser().getNickname(),
                commentDto.getContent(),
                createdAtString,
                null
        );
    }
}
