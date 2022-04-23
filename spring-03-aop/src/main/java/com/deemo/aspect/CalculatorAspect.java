package com.deemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
// 告诉 Spring 是一个 Aspect 类
@Aspect
public class CalculatorAspect {

    /**
     * 抽取公共切入点
     */
    @Pointcut("execution(public * com.deemo.calculator.DeemoCalculator.*(..))")
    public void pointCut() {}

    @Before("pointCut()")
    // @Before("public * com.deemo.calculator.DeemoCalculator.*(..)")
    public void before(JoinPoint point) {
        log.info("Method: {} before exec, the args are: {}.", this.getFullMethodName(point), point.getArgs());
    }

    @After("pointCut()")
    public void after(JoinPoint point) {
        log.info("Method: {} after exec...", this.getFullMethodName(point));
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        log.info("Method: {} after returning, the return is: {}.", this.getFullMethodName(point), result);
    }

    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) {
        log.info("Method: {} after throwing, the exception is: {}!", this.getFullMethodName(point), e);
    }

    private String getFullMethodName(JoinPoint point) {
        return point.getSignature().getDeclaringTypeName() + "#" + point.getSignature().getName();
    }

}
