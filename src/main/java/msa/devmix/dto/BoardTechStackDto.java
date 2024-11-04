package msa.devmix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.board.BoardTechStack;
import msa.devmix.domain.common.TechStack;

@Data
@AllArgsConstructor
public class BoardTechStackDto {

    private String techStackName;
    private String imageUrl;


    public static BoardTechStackDto from(BoardTechStack boardTechStack) {
        return new BoardTechStackDto(
                boardTechStack.getTechStack().getTechStackName(),
                boardTechStack.getTechStack().getImageUrl()
        );
    }

    public static BoardTechStackDto of(String techStackName, String imageUrl) {
        return new BoardTechStackDto(techStackName, imageUrl);
    }

    public static BoardTechStackDto of(String techStackName) {
        return new BoardTechStackDto(techStackName, null);
    }

    public BoardTechStack toEntity(Board board, TechStack techStack) {
        return BoardTechStack.of(board, techStack);
    }
}
