package msa.devmix.dto.response;

import lombok.Data;
import msa.devmix.repository.query.BoardTechStackQueryDto;

import java.util.List;

@Data
public class BoardTechStackListResponse {

    private Long boardId;
    private String techStackImageUrl;

    private BoardTechStackListResponse(Long boardId, String techStackImageUrl) {
        this.boardId = boardId;
        this.techStackImageUrl = techStackImageUrl;
    }

    public static BoardTechStackListResponse of(Long boardId, String techStackImageUrl) {
        return new BoardTechStackListResponse(boardId, techStackImageUrl);
    }

    public static List<BoardTechStackListResponse> from(List<BoardTechStackQueryDto> boardTechStackQueryDtos) {
        return boardTechStackQueryDtos.stream()
                .map(boardTechStackQueryDto -> BoardTechStackListResponse.of(
                        boardTechStackQueryDto.getBoardId(),
                        boardTechStackQueryDto.getTechStackImageUrl()))
                .toList();
    }
}
