package msa.devmix.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msa.devmix.domain.user.User;
import msa.devmix.dto.CommentDto;

@Getter @Setter
@NoArgsConstructor
public class PostCommentRequest {

    @NotBlank
    private String content;


    public CommentDto toDto(Long boardId, User user, String content) {
        return CommentDto.of(boardId, user, content);
    }
}
