package com.tutorial.kafka.log.annotation;

import com.tutorial.kafka.log.RuntimeLoggerAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RuntimeLoggerAspect.class})
public @interface EnableRuntimeLogger {
}