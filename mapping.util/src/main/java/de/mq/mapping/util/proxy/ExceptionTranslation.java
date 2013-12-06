package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation on methods to define exception translation to an action class
 * Simple usage of command pattern
 * @author Admin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTranslation {
	
	/**
	 * The class of the exception that should be handled
	 * @return the exception that should be transformed to the action
	 */
	Class<? extends Exception> source() ;
	/**
	 * The action that should be invoked, when the exception  defined in source is thrown
	 * @return the action, that will be executed for the exception
	 */
	Class<? extends Action> action();
	/**
	 * The name of the resourceBundle, that can be used in the action, for localized messages
	 * @return name of the resource bundle, for a message
	 */
	String bundle() default "";
	/**
	 * A proxy that will be created, to get an empty result. Void nothing, null will be returned
	 * @return the class of the result proxy as a dummy result
	 */
	Class<?> result() default Void.class;
	/**
	 * An el expression returned as result, if the exception is thrown.
	 * Inside the expression the method parameter array (#args), the model hashMap (#model)  and the exception (#ex) can be used
	 * @return an el expression that returns the result
	 */
	String resultExpression() default ""; 

}
