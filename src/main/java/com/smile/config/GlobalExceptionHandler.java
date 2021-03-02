package com.smile.config;

import com.smile.entity.common.exception.BizException;
import com.smile.entity.common.other.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author smileplus
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = BizException.class)
    public Result bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return Result.error(e.getErrorMsg());
    }
    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return Result.error("空指针异常");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:",e);
        return Result.error("未知异常！");
    }

}
