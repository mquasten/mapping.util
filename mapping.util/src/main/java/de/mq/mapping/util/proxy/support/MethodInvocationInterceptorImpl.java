package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.Parameter;

public class MethodInvocationInterceptorImpl implements Interceptor {
	
	private final ModelRepository modelRepository;
	
	public MethodInvocationInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}

	@Override
	public Object invoke(final Method method, final Object[] args) throws Throwable {
		annotationExistsGuard(method);
		try {
		   final Class<?> clazz =  method.getAnnotation(MethodInvocation.class).clazz();
		   final List<Class<?>> paramClasses = new ArrayList<>();
		   final List<Object> paramValues = new ArrayList<>();
		   for(final Parameter parameter : method.getAnnotation(MethodInvocation.class).params()){
			   paramClasses.add(parameter.clazz());
			   handleParameterValue(args, paramValues, parameter);
		   }
		   
		   final Method targetMethod = clazz.getMethod(method.getName(), paramClasses.toArray(new Class[paramClasses.size()]));
		   
		   targetMethodExistsGuard(targetMethod);
		   return targetMethod.invoke(modelRepository.get(clazz), paramValues.toArray(new Object[paramValues.size()]));
		   // method.setAccessible(true);
		   //return method.invoke(modelRepository.get( method.getAnnotation(MethodInvocation.class).clazz()), args);
		} catch(final InvocationTargetException  ex ) {
			
		    return handleTranslation(method,  ex, args);
			
			
			
		}
		
	}

	private void handleParameterValue(final Object[] args, final List<Object> paramValues, final Parameter parameter) {
		if( parameter.originIndex() >=0 ){
			   paramValues.add(args[parameter.originIndex()]);
		   }else {
			   paramValues.add(modelRepository.beanResolver().getBeanOfType(parameter.clazz()));
		   }
	}

	private void targetMethodExistsGuard(final Method targetMethod) {
		if (targetMethod == null){
			    throw new RuntimeException("Method with Annotated params not found, please check classes within params");
		   }
	}

	private void annotationExistsGuard(final Method method) {
		if(! method.isAnnotationPresent(MethodInvocation.class) ){
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + MethodInvocation.class.getSimpleName());
		}
	}

	private Object handleTranslation(final Method method, final Throwable ex, final Object args[]) throws Throwable {
		for(final ExceptionTranslation exceptionTranslation : method.getAnnotation(MethodInvocation.class).value()) {
			
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
