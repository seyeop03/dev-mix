package msa.devmix.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.dto.PositionDto;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionResponse {

    private String positionName;

    public static PositionResponse of(String positionName) {
        return new PositionResponse(positionName);
    }

    public static List<PositionResponse> from(List<PositionDto> positionDtos) {
        return positionDtos.stream()
                .map(positionDto -> PositionResponse.of(positionDto.getPositionName()))
                .toList();
    }
}
