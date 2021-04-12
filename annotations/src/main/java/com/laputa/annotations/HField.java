package com.laputa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author by xpl, Date on 2021/4/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface HField {
    String name() default "";
}
