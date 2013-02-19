package de.mq.mapping.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetterProxyCollection {

	
	
	String name();
	
	
	@SuppressWarnings("rawtypes")
	Class<? extends Collection> collectionClass() default ArrayList.class;
	
	Class<? extends Comparator<? extends Object>> comparator() default NoComparator.class;
	
	Class<?> proxyClass();
	Class<?> converter() default NoConverter.class; 
	
	Class<?> clazz() default NoModel.class;
	
	interface NoComparator extends Comparator<Object> {

		

		
		
		
		
	}
}
