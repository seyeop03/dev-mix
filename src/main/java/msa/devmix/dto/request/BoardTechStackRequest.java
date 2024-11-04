package msa.devmix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import msa.devmix.dto.BoardTechStackDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardTechStackRequest {

    @NotBlank
    private String techStackName;

    public BoardTechStackDto toDto() {
        return BoardTechStackDto.of(techStackName, null);
    }

}
