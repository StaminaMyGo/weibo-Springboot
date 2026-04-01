package com.wei.it.weibo.web.advice;

import com.wei.it.weibo.web.dto.RespEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler
    @ResponseBody
    public RespEntity catchError(Exception ex){
        return new RespEntity(5000,ex.getMessage(),null);
    }
}
