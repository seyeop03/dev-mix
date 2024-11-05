package msa.devmix.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.dto.TechStackDto;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechStackResponse {

    private String techStackName; //기술명
    private String imageUrl;

    public static TechStackResponse of(String techStackName, String imageUrl) {
        return new TechStackResponse(techStackName, imageUrl);
    }

    public static List<TechStackResponse> from(List<TechStackDto> techStackDtos) {
        return techStackDtos.stream()
                .map(techStackDto -> TechStackResponse.of(techStackDto.getTechStackName(), techStackDto.getImageUrl()))
                .toList();
    }
}
