package com.wansir.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 声明日志注解：在需要打印日志的方法上面使用@SystemLog进行标识
 * @date 2023/5/11 22:20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    String value() default "";
}
