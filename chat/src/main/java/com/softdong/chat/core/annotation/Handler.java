package com.softdong.chat.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) // 注解将会被编译到class里面，且运行时可以度
@Target(ElementType.METHOD)//给方法用的
@Documented//可以被写入javadoc文档  
public @interface Handler {

	String value() default "";
}
