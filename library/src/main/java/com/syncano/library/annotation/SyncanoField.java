package com.syncano.library.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SyncanoField {
    String name();

    String type() default "";

    boolean readOnly() default false;

    boolean required() default false;

    boolean orderIndex() default false;

    boolean filterIndex() default false;
}

