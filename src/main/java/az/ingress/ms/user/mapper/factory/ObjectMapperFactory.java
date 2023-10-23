package az.ingress.ms.user.mapper.factory;

import az.ingress.ms.user.aspect.LogIgnoreIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

public enum ObjectMapperFactory {
    MAPPER_FACTORY;
    public ObjectMapper getLogIgnoreObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new LogIgnoreIntrospector());
        objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }
}