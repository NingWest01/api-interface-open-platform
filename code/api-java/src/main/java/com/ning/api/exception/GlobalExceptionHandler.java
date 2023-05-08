package com.ning.api.exception;

import com.ning.api.common.BaseResponse;
import com.ning.api.common.ErrorCode;
import com.ning.api.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("RuntimeException", e);
        String info = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        String field = Objects.requireNonNull(e.getBindingResult().getFieldError()).getField();

        String infoMsg = "错误参数：" + field + ",错误信息：" + info;

        return ResultUtils.error(ErrorCode.PARAMETER_ERROR, infoMsg);
    }
}
