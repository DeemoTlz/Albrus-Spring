package com.deemo.code.proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopProxy {

	@Pointcut("execution(* com.deemo.code.service.*.*(..))")
	public void pointcut() {
	}

	@Before("pointcut()")
	public void beginTransaction() {
		System.out.println("before beginTransaction");
	}

	@After("pointcut()")
	public void commit() {
		System.out.println("after commit");
	}

	@AfterReturning(value = "pointcut()", returning = "returnObject")
	public void afterReturning(JoinPoint joinPoint, Object returnObject) {
		System.out.println("afterReturning");
	}

	@AfterThrowing("pointcut()")
	public void afterThrowing() {
		System.out.println("afterThrowing afterThrowing  rollback");
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			System.out.println("around");
			return joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		} finally {
			System.out.println("around");
		}
	}

}
