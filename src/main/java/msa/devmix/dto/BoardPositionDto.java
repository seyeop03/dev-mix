package msa.devmix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.board.BoardPosition;
import msa.devmix.domain.common.Position;

@AllArgsConstructor
@Data
public class BoardPositionDto {

    private String positionName;
    private Long requiredCount; //모집 인원
    private Long currentCount; //현재 인원


    public static BoardPositionDto from(BoardPosition boardPosition) {
        return new BoardPositionDto(
                boardPosition.getPosition().getPositionName(),
                boardPosition.getRequiredCount(),
                boardPosition.getCurrentCount()
        );
    }

    public static BoardPositionDto of(String positionName, Long requiredCount, Long currentCount) {
        return new BoardPositionDto(positionName, requiredCount, currentCount);
    }

    public BoardPosition toEntity(Board board, Position position, Long requiredCount) {
        return BoardPosition.of(board, position, requiredCount, currentCount);
    }

    public BoardPosition toEntity(Board board, Position position, Long requiredCount, Long currentCount) {
        return BoardPosition.builder()
                .board(board)
                .position(position)
                .requiredCount(requiredCount)
                .currentCount(currentCount)
                .build();
    }
}
