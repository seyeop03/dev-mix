package msa.devmix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.RecruitmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserBoardsDto {

    private String title;
    private Location location;
    private LocalDateTime createdAt;
    private Long commentCount;
    private RecruitmentStatus recruitmentStatus;

    public static UserBoardsDto from(Board board) {
        return new UserBoardsDto(
                board.getTitle(),
                board.getLocation(),
                board.getCreatedAt(),
                board.getCommentCount(),
                board.getRecruitmentStatus());
    }
}
