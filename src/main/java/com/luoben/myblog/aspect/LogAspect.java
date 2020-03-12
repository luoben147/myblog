package com.luoben.myblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    /**
     *
     *      * 定义一个切入点.
     *
     *      * 解释下：
     *      * ~ 第一个 * 代表任意修饰符及任意返回值.
     *      * ~ 第二个 * 任意包名
     *      * ~ 第三个 * 代表任意方法.
     *      * ~ 第四个 * 定义在web包或者子包
     *      * ~ 第五个 * 任意方法
     *      * ~ .. 匹配任意数量的参数.
     *
     */
    @Pointcut("execution(* com.luoben.myblog.controller.*.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        logger.info("-----doBefore------");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip= request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args=joinPoint.getArgs();

        RequestLog requestLog=new RequestLog(url,ip,classMethod,args);
        logger.info("Request:{}",requestLog);
    }

    @After("log()")
    public void doAfter(){
        //logger.info("-----doAfter------");
    }

    /**
     * 返回值
     * @param result
     */
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Result:{}",result);
    }


    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
