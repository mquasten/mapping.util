package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.support.BasicNullObjectResolverImpl;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
/**
 * This Annotation defines the Conditions for ParameterInjection.
 * The Parameter is resolved by the beanFactory, can be taken from the origin method arguments, it can be converted,
 * replaced by the proxy or can be resolved by an el expression, executed on the parameter that is resolved from the used BeanFactory.
 * NullObjects can be replaced in elExpressions. 
 * @author Admin
 *
 */
public @interface Parameter {
	
	/**
	 * The type of the Parameter in methodcall
	 * @return the type of the parameter.
	 */
	Class<?> clazz() ;
	
	/**
	 * Put the parameter from original method argument with the index starting with 0.
	 * If index -1 the  method argument isn't used
	 * @return the index of the method argument or -1, when no argument of the method call is used
	 */
	int originIndex() default -1; 
	
	/**
	 * An el expression to resolve the methodparameter
	 * @return the el expression to resolve the method argument
	 */
    String el() default ""; 
	
    /**
     * The type of the elResult 
     * @return the type that is returned by the el expression 
     */
    Class<?> elResultType() default Void.class ; 
    
    /**
     * Null results of the elExpression should be replaced with NullObjects resolved by the NullObjectResolver
     * @return true fif null results should be replaced with NullObjects
     */
    boolean nvl() default false;
    
    /**
     * If a method is called on a null expression the Exception should be skiped
     * @return true if the Spel Exception for not reachable targets should be skipped
     */
    boolean skipNotReachableOnNullElException() default false;
    
    /**
     * The BeanResolver for NullObjects, it is working like the BeanResolver and is returning Default Objects for a class.
     * @return the Type of the NullResolverBean that should be used. More than one can exist.
     */
    Class<? extends NullObjectResolver> nullObjectResolver() default BasicNullObjectResolverImpl.class;
    
    /**
     * Use the DomainObject with  the given type
     * @return the type of the domainObject, that should be used
     */
    Class<?> domain() default Void.class;
    
    /**
     * Use the Proxy of that class direcktly. Used in init methods for excample to have no conflicts with an uninitialized beanFactory.
     * @return true if the proxy is used directly, else false
     */
    boolean proxy() default false;
    
    /**
     * A property object is used as Parameter
     * @return properties name.
     */
    String property() default "";
    
    /**
     * The Parameter is converted, before it is used to put it into the methodcall
     * @return the class of the converter. The converter is resolved by the beanFactory
     */
    Class<? extends Converter<?,?>> converter() default NoConverter.class;
    
    
}
