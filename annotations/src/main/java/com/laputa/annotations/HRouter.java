package com.laputa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author by xpl, Date on 2021/4/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface HRouter {
    String path();

    String group() default "";
}
