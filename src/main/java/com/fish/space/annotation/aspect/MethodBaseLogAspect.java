package com.fish.space.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author: yumingjun
 * @Date: 2023/4/21 16:21
 * @Version: 1.0.0
 * @Description:
 */

@Aspect
@Slf4j
@Configuration
@ConditionalOnWebApplication
public class MethodBaseLogAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    ThreadLocal<String> methodBaseLog = new ThreadLocal<>();

    /**
     * 定义切点为@MethodBaseLog注解的类方法
     */
    @Pointcut("@annotation(com.fish.space.annotation.MethodBaseLog)")
    public void pointCut(){}

    private String transTime(long timeMillis){
        long second = 0;
        if (timeMillis >= 1000){
            second = timeMillis / 1000;
            timeMillis  = timeMillis % 1000;
        }
        String timeStr = second + "秒" + timeMillis + "毫秒";
        return timeStr;
    }

    /**
     * 通过前置通知获取请求方法，请求地址，执行方法，请求参数并追加到日志
     * @param joinPoint
     */
    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        String beforeMethodBaseLog = "%n%nURL：%s %s%nMETHOD：%s%nPARAMS：%s%n";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURL = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());
        methodBaseLog.set(String.format(beforeMethodBaseLog, requestMethod, requestURL, method, params));
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 通过后置通知获取返回结果并追加到日志
     * @param result
     * @throws Throwable
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(Object result) throws Throwable {
        String afterReturningMethodBaseLog = "RESULT：%s%n";
        afterReturningMethodBaseLog = String.format(afterReturningMethodBaseLog, result);
        methodBaseLog.set(methodBaseLog.get() + afterReturningMethodBaseLog);
    }

    /**
     * 通过最终通知获取耗时并追加到日志
     * 输出最终日志
     */
    @After(value = "pointCut()")
    public void after() {
        if (startTime.get() == null) {
            startTime.set(System.currentTimeMillis());
        }
        String afterMethodBaseLog = "TIME：%s%n";
        afterMethodBaseLog = String.format(afterMethodBaseLog, this.transTime(System.currentTimeMillis() - startTime.get()));
        methodBaseLog.set(methodBaseLog.get() + afterMethodBaseLog);
        log.info(methodBaseLog.get());
    }

    /**
     * 通过异常通知，并追加到日志
     * @param e
     */
    @AfterThrowing(value="pointCut()",throwing="e")
    public void afterThrowing(Exception e) {
        String afterThrowingMethodBaseLog = "EXCEPTION：%s%n";
        afterThrowingMethodBaseLog = String.format(afterThrowingMethodBaseLog, e.getMessage());
        methodBaseLog.set(methodBaseLog.get() + afterThrowingMethodBaseLog);
    }
}

