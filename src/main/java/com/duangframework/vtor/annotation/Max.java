package com.duangframework.vtor.annotation;

import java.lang.annotation.*;

/**
 * 最小值验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Max {

    double value() default Double.MAX_VALUE;

    String message() default "不能大于[${value}]！";

    boolean isEmpty() default true;

}
