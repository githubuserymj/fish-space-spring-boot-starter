package com.fish.space.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.context.annotation.Configuration;


import java.lang.annotation.*;


/**
 * @Author: yumingjun
 * @Date: 2022/1/20 9:43
 * @Version: 1.0.0
 * @Description: 自定义注解输出方法的执行时间，和出入参（spring AOP）
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodBaseLog {
}
