package com.heaven7.java.visitor.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.heaven7.java.visitor.collection.CollectionVisitService.OperateManager;
import com.heaven7.java.visitor.collection.IterateControl;

/**
 * indicate the method is used independent, other-operate will not effect this method.
 * this is different with thread safe.
 * <p>in visitor library ,what is other-operate? 
 *   eg: any method of {@linkplain OperateManager}, {@linkplain IterateControl}.
 * @author heaven7
 * @since 1.1.2
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Independence {
	
	/**
	 * the value indicate the method info of independent.
	 * @return a string
	 */
	String value() default "";

}
