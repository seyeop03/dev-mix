package msa.devmix.domain.constant;

import lombok.Getter;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<String> getAllLocations() {
        return Arrays.stream(Location.values())
                .map(Location::getLocation)
                .collect(Collectors.toList());
    }

    // 한글 location 문자열을 enum으로 변환하는 정적 메서드
    public static Location fromString(String location) {
        return Arrays.stream(Location.values())
                .filter(l -> l.getLocation().equals(location))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND));
    }


}
