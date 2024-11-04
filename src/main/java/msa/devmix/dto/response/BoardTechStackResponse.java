package msa.devmix.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.dto.BoardTechStackDto;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardTechStackResponse {

    private String techStackName;
    private String imageUrl;

    public static BoardTechStackResponse of(String techStackName, String imageUrl) {
        return new BoardTechStackResponse(techStackName, imageUrl);
    }

    public static BoardTechStackResponse from(BoardTechStackDto boardTechStackDto) {
        return new BoardTechStackResponse(boardTechStackDto.getTechStackName(), boardTechStackDto.getImageUrl());
    }

    public static List<BoardTechStackResponse> from(List<BoardTechStackDto> boardTechStackDtos) {
        return boardTechStackDtos.stream()
                .map(BoardTechStackResponse::from)
                .toList();
    }
}
