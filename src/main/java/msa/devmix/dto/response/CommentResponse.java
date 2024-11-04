package msa.devmix.dto.response;


import lombok.Data;
import msa.devmix.dto.CommentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentResponse {

    private Long id;
    private String nickname;
    private String profileImage;
    private String content;
    private String createdAt;

    private CommentResponse(Long id, String nickname, String profileImage, String content, String createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentResponse of(Long id, String nickname, String profileImage, String content, String createdAt) {
        return new CommentResponse(id, nickname, profileImage, content, createdAt);
    }

    public static CommentResponse from(CommentDto commentDto) {
        LocalDateTime createdAt = commentDto.getCreatedAt();
        String convertCreatedAtToString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return CommentResponse.of(
                commentDto.getId(),
                commentDto.getUser().getNickname(),
                commentDto.getUser().getProfileImage(),
                commentDto.getContent(),
                convertCreatedAtToString
        );
    }
}
