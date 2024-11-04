package msa.devmix.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import msa.devmix.dto.BoardPositionDto;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardPositionRequest {

    private String positionName;

    @Min(1) @Max(5)
    private Long requiredCount;
    private Long currentCount;

    public BoardPositionDto toDto() {
        return BoardPositionDto.of(
                positionName,
                requiredCount,
                currentCount
        );
    }
}
