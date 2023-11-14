package az.ingress.ms.user.util;

import az.ingress.ms.user.annotation.LogIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Parameter;

public enum LogUtils {
    LOG_UTILS;

    public StringBuilder buildParameters(MethodSignature signature, Object[] args, ObjectMapper objectMapper) {
        var builder = new StringBuilder();
        var parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            var currentParam = parameters[i];
            if (currentParam.isAnnotationPresent(LogIgnore.class)) {
                continue;
            }
            builder
                    .append(" ")
                    .append(currentParam.getName())
                    .append(":")
                    .append(writeObjectAsString(args[i], currentParam, objectMapper));
        }
        return builder;
    }

    private String writeObjectAsString(Object obj, Parameter parameter, ObjectMapper objectMapper) {
        return parameter.getType().getTypeName() + getObjectAsString(obj, objectMapper);
    }

    private String getObjectAsString(Object obj, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(obj).replace("\"", " ");
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }

    public String formatResponse(Object response, ObjectMapper objectMapper) {
        return getObjectAsString(response, objectMapper);
    }
}