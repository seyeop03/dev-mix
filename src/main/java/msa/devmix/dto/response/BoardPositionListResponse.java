package msa.devmix.dto.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.repository.query.BoardPositionQueryDto;

import java.util.Collections;
import java.util.List;

@Data
@Slf4j
public class BoardPositionListResponse {

    private Long boardId;
    private String positionName; // 포지션 명
    private Long requiredCount; //모집 인원
    private Long currentCount; //현재 인원

    private BoardPositionListResponse(Long boardId, String positionName, Long requiredCount, Long currentCount) {
        this.boardId = boardId;
        this.positionName = positionName;
        this.requiredCount = requiredCount;
        this.currentCount = currentCount;
    }

    public static BoardPositionListResponse of(Long boardId, String positionName, Long requiredCount, Long currentCount) {
        return new BoardPositionListResponse(boardId, positionName, requiredCount, currentCount);
    }

    public static List<BoardPositionListResponse> from(List<BoardPositionQueryDto> boardPositionQueryDtos) {
        return boardPositionQueryDtos == null ? Collections.emptyList() : boardPositionQueryDtos.stream()
                .map(boardPositionQueryDto -> BoardPositionListResponse.of(
                        boardPositionQueryDto.getBoardId(),
                        boardPositionQueryDto.getPositionName(),
                        boardPositionQueryDto.getRequiredCount(),
                        boardPositionQueryDto.getCurrentCount()))
                .toList();
    }

}
