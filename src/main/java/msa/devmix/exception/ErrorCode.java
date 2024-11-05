package msa.devmix.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //4xx 사용자 요청 오류
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed."),
    AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "Authorization Failed."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token not founded."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token not validated."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "No permission."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "Board not founded."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"Comment not founded"),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND,"Apply not founded"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "This uri is not founded."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "Duplicated nickname."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "This method is not supported."),
    TECH_STACK_NOT_FOUND(HttpStatus.NOT_FOUND,"TechStack not founded"),
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND,"Position not founded"),
    POSITION_NOT_MATCH(HttpStatus.NOT_FOUND,"Position not matched."),
    POSITION_FULL(HttpStatus.FORBIDDEN, "Position full."),
    LOCATION_NOT_FOUND(HttpStatus.FORBIDDEN, "Location not found."),

    SHORT_URL_NOT_FOUND(HttpStatus.NOT_FOUND,"Short url not founded."),


    //5xx 서버 오류
    SHORTEN_URL_KEY_EXHAUSTED(HttpStatus.SERVICE_UNAVAILABLE, "Shorten url key exhausted."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error.")
    ;

    private final HttpStatus status;
    private final String message;
}
