package com.heaven7.java.visitor.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.ElementType.*;

/**
 * indicate the field or parameter or method result can be null.
 * @author heaven7
 *
 */
@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface Nullable {
}