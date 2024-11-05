package msa.devmix.dto.response;

import lombok.Data;
import msa.devmix.domain.board.BoardTechStack;

import java.util.List;

@Data
public class BoardTechStackListResponseTest {

    private Long boardId;
    private String techStackImageUrl;

    private BoardTechStackListResponseTest(Long boardId, String techStackImageUrl) {
        this.boardId = boardId;
        this.techStackImageUrl = techStackImageUrl;
    }

    public static BoardTechStackListResponseTest of(Long boardId, String techStackImageUrl) {
        return new BoardTechStackListResponseTest(boardId, techStackImageUrl);
    }

    public static List<BoardTechStackListResponseTest> from(List<BoardTechStack> boardTechStacks) {
        return boardTechStacks.stream()
                .map(boardTechStack -> BoardTechStackListResponseTest.of(
                        boardTechStack.getId(),
                        boardTechStack.getTechStack().getImageUrl()))
                .toList();
    }
}
