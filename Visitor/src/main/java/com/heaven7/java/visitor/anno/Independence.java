package com.heaven7.java.visitor.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate the method is used independent, other operate will not effect this method.
 * this is different with thread safe.
 * @author heaven7
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface Independence {
	

}
