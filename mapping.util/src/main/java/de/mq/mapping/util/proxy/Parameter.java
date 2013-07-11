package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Parameter {
	
	Class<?> clazz() ;
	
	int originIndex() default -1; 
	
    String el() default ""; 
	
    Class<?> elResultType() default Void.class ; 
    
    boolean skipNotReachableOnNullElException() default false;
    
    Class<?> domain() default Void.class;
    
    String property() default "";
}
