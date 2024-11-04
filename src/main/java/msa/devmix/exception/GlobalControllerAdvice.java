package msa.devmix.exception;

import lombok.extern.slf4j.Slf4j;
import msa.devmix.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    //커스텀 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ResponseDto.error(e.getErrorCode().name(), e.getMessage()));
    }

    //검증 예외
    @ExceptionHandler({HandlerMethodValidationException.class,
                    MethodArgumentNotValidException.class,
                    HttpMessageNotReadableException.class}
    )
    public ResponseEntity<?> validationExceptionHandler(Exception e) {
        log.info("Error occurs {}", e.toString());
        return ResponseEntity
                .status(ErrorCode.VALIDATION_FAILED.getStatus())
                .body(ResponseDto.error(ErrorCode.VALIDATION_FAILED.name(), ErrorCode.VALIDATION_FAILED.getMessage()));
    }

    //DB 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> databaseExceptionHandler(IllegalArgumentException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity
                .status(ErrorCode.DATABASE_ERROR.getStatus())
                .body(ResponseDto.error(ErrorCode.DATABASE_ERROR.name(), ErrorCode.DATABASE_ERROR.getMessage()));
    }

    //잘못된 uri 요청 예외
    @ExceptionHandler({NoHandlerFoundException.class,
                        NoResourceFoundException.class})
    public ResponseEntity<?> resourceExceptionHandler(Exception e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseDto.error(ErrorCode.RESOURCE_NOT_FOUND.name(), ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
    }

    //잘못된 method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseDto.error(ErrorCode.METHOD_NOT_ALLOWED.name(), ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }
}
