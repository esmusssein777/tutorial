package com.tutorial.kafka.log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tutorial.kafka.log.annotation.LogRuntimeLogger;
import com.tutorial.kafka.log.annotation.LogTag;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

@Aspect
@Slf4j
public class RuntimeLoggerAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Around("execution(* com.tutorial..*Controller.*(..))")
    Object logRuntimeInformation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        Method method = Arrays.stream(joinPoint.getSignature().getDeclaringType().getDeclaredMethods()).filter(m -> m.getName().equals(joinPoint.getSignature().getName())).findFirst().get();
        RuntimeInformation runtimeInformation = null;
        try {
            if (method.getDeclaringClass().isAnnotationPresent(LogRuntimeLogger.class)) {
                runtimeInformation = new RuntimeInformation();
                LogTag logTag = method.getAnnotation(LogTag.class);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                runtimeInformation.setTag(logTag != null ? logTag.value() : LogTag.DEFAULT_TAG);
                runtimeInformation.setApplicationName(this.applicationName);
                runtimeInformation.setClassName(method.getDeclaringClass().getName());
                runtimeInformation.setMethod(method.getName());
                runtimeInformation.setRequestUrl(request != null ? request.getRequestURL().toString() : "NONE");
                runtimeInformation.setRequestMethod(request != null ? request.getMethod() : "NONE");
            }
            result = joinPoint.proceed();
        } catch (Throwable t) {
            if (runtimeInformation != null) {
                runtimeInformation.setExceptionMessage(t.getMessage());
                runtimeInformation.setExceptionStack(String.join("\r\n", Arrays.stream(t.getStackTrace()).reduce(new ArrayList<String>(), (stackList, stack) -> {
                    stackList.add(stack.toString());
                    return stackList;
                }, (a, b) -> new ArrayList<>())));
            }
            throw t;
        } finally {
            if (runtimeInformation != null) {
                runtimeInformation.complete();
                String topic = method.getDeclaringClass().getAnnotation(LogRuntimeLogger.class).topic();
                if (!topic.isEmpty()) {
                    kafkaTemplate.send(topic, JSONObject.toJSONStringWithDateFormat(runtimeInformation, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
                }
            }
        }
        return result;
    }
}
