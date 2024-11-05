package msa.devmix.dto.response;

import lombok.Data;
import msa.devmix.domain.board.BoardPosition;

import java.util.List;

@Data
public class BoardPositionListResponseTest {

    private Long boardId;
    private String positionName;
    private Long requiredCount; //모집 인원
    private Long currentCount;

    private BoardPositionListResponseTest(Long boardId, String positionName, Long requiredCount, Long currentCount) {
        this.boardId = boardId;
        this.positionName = positionName;
        this.requiredCount = requiredCount;
        this.currentCount = currentCount;
    }

    public static BoardPositionListResponseTest of(Long boardId, String positionName, Long requiredCount, Long currentCount) {
        return new BoardPositionListResponseTest(boardId, positionName, requiredCount, currentCount);
    }

    public static List<BoardPositionListResponseTest> from(List<BoardPosition> boardPositions) {
        return boardPositions.stream()
                .map(boardPosition -> BoardPositionListResponseTest.of(
                        boardPosition.getBoard().getId(),
                        boardPosition.getPosition().getPositionName(),
                        boardPosition.getRequiredCount(),
                        boardPosition.getCurrentCount()))
                .toList();
    }
}
