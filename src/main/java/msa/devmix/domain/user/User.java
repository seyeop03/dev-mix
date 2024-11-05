package msa.devmix.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.constant.Role;
import msa.devmix.dto.UserWithPositionTechStackDto;


@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username; //로그인 ID
    private String password; //패스워드 (소셜 로그인 시 dummy)
    private String nickname; //닉네임
    private String email; //이메일
    private String groupName; //소속  e.g.,학교, 회사 등
    private String location; //거주지  ex) "니맘속" 같은 예상하지 못한 문자열이 들어올 수 있기 때문에 String
    private String provider; //registrationId (google, kakao, naver)
    private String providerId; //sub, id 값
    private String profileImage; //프로필 이미지

    @Enumerated(EnumType.STRING)
    private Role role; //유저 role

    private User(String username, String password, String nickname, String email, String groupName, String location, String provider, String providerId, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.groupName = groupName;
        this.location = location;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    private User(Long id, String username, String nickname, String email, String groupName, String profileImage) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.groupName = groupName;
        this.profileImage = profileImage;
    }

    public static User of(String username, String password, String nickname, String email, String groupName, String location, String provider, String providerId, Role role) {
        return new User(username, password, nickname, email, groupName, location, provider, providerId, role);
    }

    public static User of(Long id, String username, String nickname, String email, String groupName, String profileImage) {
        return new User(id, username, nickname, email, groupName, profileImage);
    }

    public void setAdditionalUserInfo(UserWithPositionTechStackDto dto, String profileImageUrl) {
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.groupName = dto.getGroupName();
        this.location = dto.getLocation();
        this.profileImage = profileImageUrl;
        this.role = Role.ROLE_USER;
    }
}
