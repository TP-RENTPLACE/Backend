package kattsyn.dev.rentplace.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(public * kattsyn.dev.rentplace.services..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Вызов метода: {}", methodName);
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
            log.info("Метод {} выполнен успешно за {} с", methodName, elapsedTime);
            return result;
        } catch (Throwable ex) {
            double elapsedTime = (double) (System.currentTimeMillis() - startTime) / 1000;
            log.error("Метод {} завершился с ошибкой за {} с. Ошибка: {}", methodName, elapsedTime, ex.getMessage());
            throw ex;
        }
    }
}
