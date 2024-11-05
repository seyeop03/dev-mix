package msa.devmix.domain.constant;

import lombok.Getter;

@Getter
public enum Location {

    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    GANGWON("강원"),
    DAEGU_GYEONGBUK("대구/경북"),
    DAEJEON_CHUNGCHEONG("대전/충청"),
    BUSAN_ULSAN_GYEONGNAM("부산/울산/경남"),
    GWANGJU_JEOLLA("광주/전라"),
    JEJU("제주");

    private final String location;

    Location(String location) {
        this.location = location;
    }
}
