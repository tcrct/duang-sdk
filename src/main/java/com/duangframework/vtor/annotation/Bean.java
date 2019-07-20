package com.duangframework.vtor.annotation;

import java.lang.annotation.*;

/**
 * bean对象， 如果设置了，说明要转换或验证
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    String name() default "";
    String label() default "";
}

