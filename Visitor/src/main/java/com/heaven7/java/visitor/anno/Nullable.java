package com.heaven7.java.visitor.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.ElementType.*;

@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface Nullable {
}