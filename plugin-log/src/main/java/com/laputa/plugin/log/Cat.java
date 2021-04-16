package com.laputa.plugin.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author by xpl, Date on 2021/4/16.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Cat {
    String soul() default "";
}
