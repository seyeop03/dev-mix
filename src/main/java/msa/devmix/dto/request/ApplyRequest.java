package msa.devmix.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.constant.ParticipationStatus;
import msa.devmix.domain.user.User;
import msa.devmix.dto.ApplyDto;
import msa.devmix.dto.BoardPositionDto;

@Getter
@AllArgsConstructor
public class ApplyRequest {

    private String positionName; //포지션 이름
    private String note; //지원 사유 및 한마디

    /**
     * Todo: positionName 을 BoardPositionDto 로 변경하는 책임을 of 에서 해야할까?
     */
    public ApplyDto toDto(User user, Long boardId) {
        return ApplyDto.of(
            user,
            boardId,
            positionName,
            ParticipationStatus.UNKNOWN,
            note
        );
    }
}
