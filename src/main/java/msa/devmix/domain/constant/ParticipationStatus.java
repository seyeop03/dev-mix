package msa.devmix.domain.constant;

import lombok.Getter;

@Getter
public enum ParticipationStatus {
    YES("승인"), NO("거절"), UNKNOWN("미정");

    private String value;

    ParticipationStatus(String value) {
        this.value = value;
    }

    public static String getValue(String value) {
        return ParticipationStatus.valueOf(value).getValue();
    }
}
