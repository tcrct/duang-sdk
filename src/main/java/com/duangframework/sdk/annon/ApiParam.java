package com.duangframework.sdk.annon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api value注解
 * @author laotang
 *
 */
@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {
    // 用于对Restful API下的 {name} 进行替换，须保证唯一性
    String label() default "";
    // 用于对json key进行替换 类似于fastjson注解JsonField下的name
    String name() default "";
}
