package msa.devmix.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;


    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    //log.error(e.toString())
    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        } else {
            return String.format("%s -> %s", errorCode.getMessage(), message);
        }
    }
}
