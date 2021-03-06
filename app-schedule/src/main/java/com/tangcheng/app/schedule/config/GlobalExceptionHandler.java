package com.tangcheng.app.schedule.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by tangcheng on 4/30/2017.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest request, Exception e) {
        LOGGER.error("{}?{},msg:{}", request.getRequestURI(), request.getQueryString(), e.getMessage(), e);
        if (e instanceof SQLException) {
            return ResponseEntity.ok("服务器好像开小差了，小二正在处理，马上就来。客官请稍后:-) ");
        }
        return ResponseEntity.ok(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBindException(HttpServletRequest request, BindException e) {
        StringBuilder result = new StringBuilder();
        for (FieldError fieldError : e.getFieldErrors()) {
            result.append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage())
                    .append(",invalid input:")
                    .append(fieldError.getRejectedValue())
                    .append(System.getProperty("line.separator"));
        }
        LOGGER.error("{}?{},tips:{}", request.getRequestURI(), request.getQueryString(), result);
        return ResponseEntity.ok(result.toString());
    }

}
