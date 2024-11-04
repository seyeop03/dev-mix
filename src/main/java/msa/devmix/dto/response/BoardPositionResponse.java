package msa.devmix.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.dto.BoardPositionDto;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardPositionResponse {

    private String positionName;
    private Long requiredCount; //모집 인원
    private Long currentCount;

    public static BoardPositionResponse from(BoardPositionDto boardPositionDto) {
        return BoardPositionResponse.of(
                boardPositionDto.getPositionName(),
                boardPositionDto.getRequiredCount(),
                boardPositionDto.getCurrentCount());
    }

    public static BoardPositionResponse of(String positionName, Long requiredCount, Long currentCount) {
        return new BoardPositionResponse(positionName, requiredCount, currentCount);
    }

    public static List<BoardPositionResponse> from(List<BoardPositionDto> boardPositionDtos) {
        return boardPositionDtos.stream()
                .map(BoardPositionResponse::from)
                .toList();
    }
}
