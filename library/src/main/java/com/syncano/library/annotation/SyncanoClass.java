package com.syncano.library.annotation;

import com.syncano.library.data.SyncanoObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SyncanoClass {
    String name();

    int version() default 1;

    Class<? extends SyncanoObject> previousVersion() default NOT_SET.class;

    final class NOT_SET extends SyncanoObject {
    }
}
