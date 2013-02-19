package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.convert.converter.Converter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Getter {

	Class<? extends Converter<?,?>>  converter() default NoConverter.class;
	String value() ;
	
	Class<?> clazz() default NoModel.class;
	
	
	
	
}
