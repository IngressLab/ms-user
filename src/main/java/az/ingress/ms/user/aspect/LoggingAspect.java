package az.ingress.ms.user.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static az.ingress.ms.user.mapper.factory.ObjectMapperFactory.MAPPER_FACTORY;
import static az.ingress.ms.user.util.LogUtils.LOG_UTILS;
import static org.slf4j.LoggerFactory.getLogger;

@Aspect
@Component
public class LoggingAspect {

    @Around("within(@az.ingress.ms.user.annotation.Log *)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = ((MethodSignature) joinPoint.getSignature());
        var log = getLogger(joinPoint.getTarget().getClass());

        var parameters = LOG_UTILS.buildParameters(signature, joinPoint.getArgs(), MAPPER_FACTORY.getLogIgnoreObjectMapper());

        logEvent("start", log, signature, parameters);
        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Throwable throwable) {
            logEvent("error", log, signature, parameters);
            throw throwable;
        }
        logEndAction(log, signature, response);
        return response;
    }

    private void logEvent(String eventName, Logger log, MethodSignature signature, StringBuilder parameters) {
        log.info("ActionLog.{}.{}{}", signature.getName(), eventName, parameters);
    }

    private void logEndAction(Logger log, MethodSignature signature, Object response) {
        if (void.class.equals(signature.getReturnType())) log.info("ActionLog.{}.end", signature.getName());
        else
            log.info("ActionLog.{}.end {}", signature.getName(), LOG_UTILS.formatResponse(response, MAPPER_FACTORY.getLogIgnoreObjectMapper()));
    }
}