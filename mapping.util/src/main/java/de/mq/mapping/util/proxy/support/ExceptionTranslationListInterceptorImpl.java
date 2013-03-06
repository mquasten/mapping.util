package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.mapping.util.proxy.ModelRepository;

public class ExceptionTranslationListInterceptorImpl implements Interceptor {
	
	private final ModelRepository modelRepository;
	
	public ExceptionTranslationListInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}

	@Override
	public Object invoke(final Method method, final Object[] args) throws Throwable {
		
		try {
		   method.setAccessible(true);
		   return method.invoke(modelRepository.get( method.getAnnotation(ExceptionTranslations.class).clazz()), args);
		} catch(final InvocationTargetException  ex ) {
			
		    return handleTranslation(method,  ex, args);
			
			
			
		}
		
	}

	private Object handleTranslation(final Method method, final Throwable ex, final Object args[]) throws Throwable {
		for(final ExceptionTranslation exceptionTranslation : method.getAnnotation(ExceptionTranslations.class).value()) {
			
			    if (exceptionTranslation.source().isInstance(ex.getCause())) {
			       return action(exceptionTranslation).execute(exceptionTranslation, modelRepository, ex.getCause(), args );
			    }
			   
		}
		throw ex.getCause();
		
	}

	

	

	private Action action (final ExceptionTranslation exceptionTranslation) throws Exception {
		
		return modelRepository.beanResolver().getBeanOfType(exceptionTranslation.action());
	/*	try {
			return exceptionTranslation.action().getDeclaredConstructor().newInstance();
		} catch (final Exception ex ) {
			throw new IllegalStateException("Unable to create action for ExceptionTranslation" , ex);
		} */
	}

}
