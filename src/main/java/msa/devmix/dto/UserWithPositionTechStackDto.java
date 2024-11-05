package msa.devmix.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import msa.devmix.domain.user.User;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserWithPositionTechStackDto {

    private Long userId;
    private String nickname;
    private String email;

    @Setter
    private String profileImage; //조회시만 사용
    private String groupName;
    private String location;

    private List<String> positions;
    private List<TechStackDto> techStacks;

    //조회용
    public static UserWithPositionTechStackDto of(User user, List<String> positions, List<TechStackDto> techStacks) {
        return new UserWithPositionTechStackDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImage(),
                user.getGroupName(),
                user.getLocation(),
                positions,
                techStacks
        );
    }

    //저장용
    public static UserWithPositionTechStackDto of(User user,
                                                  String nickname,
                                                  String email,
                                                  String groupName,
                                                  String location,
                                                  List<String> positions,
                                                  List<TechStackDto> techStacks) {
        return new UserWithPositionTechStackDto(
                user.getId(),
                nickname,
                email,
                null,
                groupName,
                location,
                positions,
                techStacks
        );
    }
}
