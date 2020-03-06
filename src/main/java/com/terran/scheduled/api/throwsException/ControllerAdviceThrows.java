package com.terran.scheduled.api.throwsException;

import com.terran.scheduled.api.utils.JsonResult;
import org.hibernate.TypeMismatchException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice
@ResponseBody
public class ControllerAdviceThrows {
    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException ex) {
        return JsonResult.fail("运行时异常" + ex.getMessage());
    }
    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException ex) {
        return JsonResult.fail("空指针异常" + ex.getMessage());
    }
    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public String classCastExceptionHandler(ClassCastException ex) {
        return JsonResult.fail("类型转换异常" + ex.getMessage());
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public String iOExceptionHandler(IOException ex) {
        return JsonResult.fail("IO异常" + ex.getMessage());
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public String noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return JsonResult.fail("未知方法异常" + ex.getMessage());
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public String indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return JsonResult.fail("数组越界异常" + ex.getMessage());
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public String requestNotReadable(HttpMessageNotReadableException ex) {
        return JsonResult.fail("400http信息不可读异常" + ex.getMessage());
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    public String requestTypeMismatch(TypeMismatchException ex) {
        return JsonResult.fail("400请求类型不匹配异常" + ex.getMessage());
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String requestMissingServletRequest(MissingServletRequestParameterException ex) {
        return JsonResult.fail("400请求丢失servlet参数异常" + ex.getMessage());
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public String request405(HttpRequestMethodNotSupportedException ex) {
        return JsonResult.fail("405不允许此方法异常" + ex.getMessage());
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public String request406(HttpMediaTypeNotAcceptableException ex) {
        return JsonResult.fail("406客户端请求期望响应的媒体类型与服务器响应的媒体类型不一致" + ex.getMessage());
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public String server500(RuntimeException ex) {
        return JsonResult.fail("500服务端内部错误异常" + ex.getMessage());
    }

    //栈溢出
    @ExceptionHandler({StackOverflowError.class})
    public String requestStackOverflow(StackOverflowError ex) {
        return JsonResult.fail("栈溢出异常" + ex.getMessage());
    }

    //除数不能为0
    @ExceptionHandler({ArithmeticException.class})
    public String arithmeticException(ArithmeticException ex) {
        return JsonResult.fail("计算异常" + ex.getMessage());
    }


    //其他错误
    @ExceptionHandler({Exception.class})
    public String exception(Exception ex) {
        return JsonResult.fail("异常" + ex.getMessage());
    }
}
