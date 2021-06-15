package com.hendisantika.jsoupdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Project : jsoup-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 15/06/21
 * Time: 07.59
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(MissingServletRequestParameterException exception) {
        LOG.error(exception.getMessage(), exception);
        return error(exception.getMessage());
    }
}
