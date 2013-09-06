package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public  @interface ActionEvent {
	
	Parameter[] params() default {};
	
	String name() default "" ;
	
	
	boolean startConversation() default false;
	

}
