package com.mengnankk.designpattern;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Aspect
@Component
public class LogAspect {

    @Before("@annotation(com.mengnankk.designpattern.Log)") // 指定注解全路径
    public void logBefore(JoinPoint point) {
        System.out.println("调用方法：" + point.getSignature().getName());
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
 @interface Log {
    String value() default "";
}

