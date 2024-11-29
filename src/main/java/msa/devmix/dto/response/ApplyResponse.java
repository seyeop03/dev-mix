package msa.devmix.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.constant.ParticipationStatus;
import msa.devmix.dto.ApplyDto;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplyResponse {

    private String userNickname;
    private Long boardId;
    private String boardTitle;
    private String positionName;
    private String participationStatus;
    private String note;
    private String applyDate;

    public static ApplyResponse of(
            String userNickname,
            Long boardId,
            String boardTitle,
            String positionName,
            String participationStatus,
            String note,
            String applyDate) {
        return new ApplyResponse(
                userNickname,
                boardId,
                boardTitle,
                positionName,
                participationStatus,
                note,
                applyDate);
    }

    public static ApplyResponse from(ApplyDto applyDto) {
        String participationStatus = applyDto.getParticipationStatus().toString();
        String applyDate = applyDto.getApplyDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return ApplyResponse.of(
                applyDto.getUser().getNickname(),
                applyDto.getBoardId(),
                applyDto.getBoardTitle(),
                applyDto.getPositionName(),
                ParticipationStatus.getValue(participationStatus),
                applyDto.getNote(),
                applyDate);
    }
}
