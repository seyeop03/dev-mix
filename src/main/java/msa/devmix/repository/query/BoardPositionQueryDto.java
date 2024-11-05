package msa.devmix.repository.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPositionQueryDto {

    private Long boardId;
    private String positionName; // 포지션 명
    private Long requiredCount; //모집 인원
    private Long currentCount; //현재 인원


}
