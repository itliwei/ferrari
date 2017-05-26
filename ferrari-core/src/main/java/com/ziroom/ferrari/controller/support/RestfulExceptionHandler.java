package com.ziroom.ferrari.controller.support;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ziroom.ferrari.constants.ErrorCode;
import com.ziroom.ferrari.exception.FerrariException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dongh38@ziroom
 */
@ControllerAdvice
public class RestfulExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {FerrariException.class})
    public final ResponseEntity<ErrorResult> handleServiceException(FerrariException ex,
                                                                    HttpServletRequest request) {
        logError(ex, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
        ErrorResult result = new ErrorResult(ex.errorCode.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public final ResponseEntity<ErrorResult> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        logError(ex,request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
        ErrorResult result = new ErrorResult(ErrorCode.ARGUMENT_ERROR.getErrorCode(),ex.getMessage());
        return new ResponseEntity<>(result,headers, HttpStatus.OK);
    }

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<ErrorResult> handleGeneralException(Exception ex, HttpServletRequest request) {
        logError(ex, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
        ErrorResult result = new ErrorResult(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
        return new ResponseEntity<>(result, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        logError(ex);

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }

    public void logError(Exception ex) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        logger.error(JSON.toJSONString(map), ex);
    }

    public void logError(Exception ex, HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        map.put("from", request.getRemoteAddr());
        String queryString = request.getQueryString();
        map.put("path", queryString != null ?
                (request.getRequestURI() + "?" + queryString) :
                request.getRequestURI());

        logger.error(JSON.toJSONString(map), ex);
    }
}
