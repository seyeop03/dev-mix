package msa.devmix.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private String code;
    private T result;
    private String message;


    public static <T> ResponseDto<T> success() {
        return new ResponseDto<T>("SUCCESS", null, null);
    }

    public static <T> ResponseDto<T> success(T result) {
        return new ResponseDto<T>("SUCCESS", result, null);
    }

    public static ResponseDto<Void> error(String code, String message) {
        return new ResponseDto<>(code, null, message);
    }
}
