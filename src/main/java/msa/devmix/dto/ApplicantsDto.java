package msa.devmix.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.board.Apply;
import msa.devmix.domain.constant.ParticipationStatus;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicantsDto {

    private String userNickname;
    private String positionName;
    private Long boardId;
    private String boardTitle;
    private String applyNote;
    private String applyDate;
    private String participationStatus;

    public static ApplicantsDto of(String userNickname,
                                   String positionName,
                                   Long boardId,
                                   String boardTitle,
                                   String applyNote,
                                   String applyDate,
                                   String participationStatus) {
        return new ApplicantsDto(userNickname, positionName, boardId, boardTitle, applyNote, applyDate, participationStatus);
    }

    public static ApplicantsDto from(Apply apply) {
        String applyDateString = apply.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String status = apply.getParticipationStatus().toString();
        return ApplicantsDto.of(
                apply.getUser().getNickname(),
                apply.getBoardPosition().getPosition().getPositionName(),
                apply.getBoardPosition().getBoard().getId(),
                apply.getBoardPosition().getBoard().getTitle(),
                apply.getNote(),
                applyDateString,
                ParticipationStatus.getValue(status)
                );
    }
}
