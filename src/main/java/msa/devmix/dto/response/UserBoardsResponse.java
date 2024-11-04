package msa.devmix.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.RecruitmentStatus;
import msa.devmix.dto.UserBoardsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class UserBoardsResponse {

    private String title;
    private Location location;
    private String createdAt;
    private Long commentCount;
    private RecruitmentStatus recruitmentStatus;

    public static UserBoardsResponse from(UserBoardsDto userBoardsDto) {
        String createdAtToString = userBoardsDto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new UserBoardsResponse(
                userBoardsDto.getTitle(),
                userBoardsDto.getLocation(),
                createdAtToString,
                userBoardsDto.getCommentCount(),
                userBoardsDto.getRecruitmentStatus()
        );
    }

}
