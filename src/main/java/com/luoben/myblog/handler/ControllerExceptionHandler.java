package com.luoben.myblog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    //日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)//指定拦截异常的类型
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e){

        //日志记录错误信息
        logger.error("Request Url : {}, Exception : {}",request.getRequestURL(),e.getMessage());

        //返回内容
        ModelAndView mv = new ModelAndView();
        mv.addObject("url:", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return  mv;
    }

}
