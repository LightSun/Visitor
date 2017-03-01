package com.heaven7.java.visitor.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate the effect of invoke this method depend on some setting. 
 * @author heaven7
 * @since 1.1.2
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface DependOn {

	/**
	 * indicate the depend info of the method invoke , and this will help user fast understand the meaning of design.
	 * @return the depend info, such as class name, method name.
	 * @since 1.1.2
	 */
	String[] value() default {};
	
	/**
	 * indicate the method depend on the all classes.  
	 * @return the all classes.
	 * @since 1.1.2
	 */
	Class<?>[] classes() default {};
}
