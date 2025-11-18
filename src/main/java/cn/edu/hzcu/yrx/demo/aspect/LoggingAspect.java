package cn.edu.hzcu.yrx.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.addStudent(..))")
    public void addStudentPointcut() {}

    @Pointcut("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.deleteStudent(..))")
    public void deleteStudentPointcut() {}

    @Pointcut("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.modifyStudent(..))")
    public void modifyStudentPointcut() {}

    @Pointcut("execution(* cn.edu.hzcu.yrx.demo.service.StudentService.findStudent(..))")
    public void findStudentPointcut() {}

    @Around("addStudentPointcut() || deleteStudentPointcut() || modifyStudentPointcut() || findStudentPointcut()")
    public Object logServiceMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        logger.info("=== AOP日志 === 调用方法: {}, 参数: {}", methodName, Arrays.toString(args));
        
        try {
            Object result = joinPoint.proceed();
            logger.info("=== AOP日志 === 方法 {} 执行成功, 返回值: {}", methodName, result);
            return result;
        } catch (Exception e) {
            logger.error("=== AOP日志 === 方法 {} 执行失败: {}", methodName, e.getMessage());
            throw e;
        }
    }
}
