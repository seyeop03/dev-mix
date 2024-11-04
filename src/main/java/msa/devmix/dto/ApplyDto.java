package msa.devmix.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.board.Apply;
import msa.devmix.domain.board.BoardPosition;
import msa.devmix.domain.constant.ParticipationStatus;
import msa.devmix.domain.user.User;

@Getter
@AllArgsConstructor
public class ApplyDto {

    //연관관계 필드
    private User user; //N:1
    private Long boardId;
    private String positionName;

    //일반 필드
    private ParticipationStatus participationStatus; //참여 여부
    private String note;

    public static ApplyDto of(User user, Long boardId, String positionName, ParticipationStatus participationStatus, String note) {
        return new ApplyDto(user, boardId, positionName, participationStatus, note);
    }

    public Apply toEntity(User user, BoardPosition boardPosition) {
        return Apply.of(user, boardPosition, participationStatus, note);
    }
}
