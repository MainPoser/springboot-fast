package cn.com.datu.springboot.oauth2server.common.exception.advice;


import cn.com.datu.springboot.oauth2server.common.entity.ApiResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tianyao
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 用来处理bean validation异常
     * @param ex
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class,MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiResponse<String> resolveConstraintViolationException(Exception ex){
        log.error("paramsException:<{}>", ExceptionUtils.getFullStackTrace(ex));
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        if (ex instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException exception= (MethodArgumentNotValidException) ex;
            List<String> errors = new ArrayList<>();
            BindingResult bindingResult = exception.getBindingResult();
            bindingResult.getAllErrors().forEach(error->errors.add(error.getDefaultMessage()));
            apiResponse.setMessage(JSON.toJSONString(errors));
        }else {
            ConstraintViolationException exception= (ConstraintViolationException) ex;
            String message = exception.getMessage();
            apiResponse.setMessage(message);
        }
        return apiResponse;
    }


    /**
     * servletException
     *包括参数校验异常
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            BindException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ApiResponse<String> handleServletException(Exception e) {
        log.error("servletException:<{}>",ExceptionUtils.getFullStackTrace(e));
        String code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String message = e.getMessage();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponse<String> handleException(Exception e) {
        log.error("unKnowException:<{}>",ExceptionUtils.getFullStackTrace(e));
        String code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String message = e.getMessage();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }
}