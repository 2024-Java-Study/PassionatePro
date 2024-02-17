package com.example.pro.common;

import com.example.pro.auth.exception.AuthException;
import com.example.pro.board.exception.NoSearchBoardException;
import com.example.pro.common.response.BasicResponse;
import com.example.pro.common.response.ErrorEntity;
import com.example.pro.common.response.ResponseUtil;
import com.example.pro.files.S3IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BasicResponse<ErrorEntity> authException(AuthException e) {
        log.error("Auth Exception({})={}", e.getCode(), e.getMessage());
        return ResponseUtil.error(new ErrorEntity(e.getCode().toString(), e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BasicResponse<ErrorEntity> s3Exception(S3IOException e) {
        log.error("S3 IOException({})={}", e.getErrorCode(), e.getMessage());
        return ResponseUtil.error(new ErrorEntity(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicResponse<ErrorEntity> validationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        log.error("Dto Validation Exception({}): {}", "BAD_INPUT", errors);
        return ResponseUtil.error(new ErrorEntity("BAD_INPUT", "입력이 올바르지 않습니다.", errors));
    }

    // board
    @ExceptionHandler(NoSearchBoardException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 상태 코드 204...?
    public BasicResponse<ErrorEntity> boardException(NoSearchBoardException e) {
        log.error("Board Exception({})={}", e.getCode(), e.getMessage());
        return ResponseUtil.error(new ErrorEntity(e.getCode().toString(), e.getMessage()));
    }
}
