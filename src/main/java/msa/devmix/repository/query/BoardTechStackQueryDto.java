package msa.devmix.repository.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardTechStackQueryDto {

    private Long boardId;
    private String techStackImageUrl;
}
