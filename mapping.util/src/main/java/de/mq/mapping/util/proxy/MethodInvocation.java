package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodInvocation {
	
	ExceptionTranslation[] value() default {}; 
	
	Class<?> clazz() ;
	
	ActionEvent[] actions() default {}; 

}
