package msa.devmix.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.common.Position;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionDto {

    private String positionName;

    public static PositionDto of(String positionName) {
        return new PositionDto(positionName);
    }

    public static PositionDto from(Position position) {
        return PositionDto.of(position.getPositionName());
    }
}
