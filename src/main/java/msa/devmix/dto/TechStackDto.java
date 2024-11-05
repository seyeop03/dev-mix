package msa.devmix.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.common.TechStack;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechStackDto {

    private Long techStackId;
    private String techStackName; //기술명
    private String imageUrl; //해당 기술 이미지 URL

    public static TechStackDto of(String techStackName) {
        return new TechStackDto(null, techStackName, null);
    }

    private TechStackDto(String techStackName, String imageUrl) {
        this.techStackName = techStackName;
        this.imageUrl = imageUrl;
    }

    public static TechStackDto of(String techStackName, String imageUrl) {
        return new TechStackDto(techStackName, imageUrl);
    }

    public static TechStackDto from(TechStack techStack) {
        return TechStackDto.of(techStack.getTechStackName(), techStack.getImageUrl());
    }
}
