package az.ingress.ms.user.aspect;

import az.ingress.ms.user.annotation.LogIgnore;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import static java.util.Objects.nonNull;

public class LogIgnoreIntrospector extends JacksonAnnotationIntrospector {
    @Override
    public boolean hasIgnoreMarker(AnnotatedMember member) {
        var logIgnore = member.getAnnotation(LogIgnore.class);
        if (nonNull(logIgnore)) return true;
        return super.hasIgnoreMarker(member);
    }
}