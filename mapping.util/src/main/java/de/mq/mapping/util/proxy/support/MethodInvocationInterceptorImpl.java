package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Conversation;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoModel;
import de.mq.mapping.util.proxy.Parameter;

public class MethodInvocationInterceptorImpl implements Interceptor {
	
	private static final String ARG = "arg";
	private final ModelRepository modelRepository;
	
	public MethodInvocationInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}

	@Override
	public Object invoke(final Method method, final Object[] args) throws Throwable {
		annotationExistsGuard(method);
		try {
		  
		  
		   Object result=null; 
		   boolean likeAVirgin=true;
		   for(final ActionEvent action : method.getAnnotation(MethodInvocation.class).actions()){
			   final Class<?> clazz = clazz(method.getAnnotation(MethodInvocation.class), action);
		
			   if( action.startConversation()){
				   modelRepository.beanResolver().getBeanOfType(Conversation.class).begin();
			   }
			   
			  final Object methodResult = handleActionEvent(method, args, clazz, action);
			  
			  if ( ! method.getReturnType().equals(Void.TYPE)){
				   result=methodResult;
			  }
			  
			  if( action.endConversation()){
				   modelRepository.beanResolver().getBeanOfType(Conversation.class).end();
			   }
			  
			  likeAVirgin=false;
		   }
		   
		   if( likeAVirgin){
			 
			   throw new IllegalArgumentException("No action mapped, please check annotations method: " + method.getDeclaringClass() + "." + method.getName());
		   }
		   return result;
		   // method.setAccessible(true);
		   //return method.invoke(modelRepository.get( method.getAnnotation(MethodInvocation.class).clazz()), args);
		} catch(final InvocationTargetException  ex ) {
			
		    return handleTranslation(method,  ex, args);
			
			
			
		}
		
	}

	private Class<?> clazz(final MethodInvocation methodInvocation, final ActionEvent action) {
		if( action.clazz() != Void.class){
			   return action.clazz();
		}
		return methodInvocation.clazz();
	}

	private Object handleActionEvent(final Method method, final Object[] args, final Class<?> clazz, final ActionEvent action) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		final List<Class<?>> paramClasses = new ArrayList<>();
		final List<Object> paramValues = new ArrayList<>();
		
		
		
		for (final Parameter parameter : action.params()) {
			paramClasses.add(parameterClass(parameter)); 
			paramValues.add(handleParameterValue(args,parameter));
		}
		
		
		final Method targetMethod = clazz.getDeclaredMethod(methodName(method, action), paramClasses.toArray(new Class[paramClasses.size()]));	
		targetMethod.setAccessible(true);
		return targetMethod.invoke(modelRepository.get(clazz), paramValues.toArray(new Object[paramValues.size()]));
		
	}

	private Class<?> parameterClass(final Parameter parameter) {
		if(  parameter.el().trim().length() == 0 ) {
		    return parameter.clazz();
		   
		} else {
			return parameter.elResultType();
		}
	}

	private String methodName(final Method method, final ActionEvent action) {
		if ( action.name().trim().length()>0){
			   return action.name();
		}
		return method.getName();
	}

	@SuppressWarnings("unchecked")
	private Object handleParameterValue(final Object[] args, final Parameter parameter) {
		final Object managedBean = getBean(args, parameter);
		
		beanExistsGuard(managedBean);
		
		if( parameter.el().trim().length() == 0 ){
			   return convert(managedBean, (Class<? extends Converter<Object, Object>>) parameter.converter());   
		} 
		
		 
		return convert(modelRepository.beanResolver().getBeanOfType(ELExpressionParser.class).withVariable(ARG, managedBean).withExpression(parameter.el()).withSkipNotReachableOnNullPropertyException(parameter.skipNotReachableOnNullElException()).parse(),   (Class<? extends Converter<Object, Object>>) parameter.converter());
	}

	private Object convert(final Object  result,  Class<? extends Converter<Object,Object>>  clazz) {
		return modelRepository.beanResolver().getBeanOfType(clazz).convert(result);
	}

	private Object getBean(final Object[] args, final Parameter parameter) {
		
		
		if( parameter.originIndex() >=0 ){
			return args[parameter.originIndex()];
			 
		}
		if(parameter.proxy()){
			return modelRepository.proxy();
		}
		
	    if( parameter.domain() != Void.class){
	    	return  modelRepository.get(parameter.domain());
	    }
	    
	    if( parameter.property().trim().length() != 0 ) {
	    	return modelRepository.get(NoModel.class, parameter.property());
	    }
	 
	    
	    return modelRepository.beanResolver().getBeanOfType(parameter.clazz());
	}

	private void beanExistsGuard( final Object managedBean) {
		if( managedBean==null){
			throw new IllegalArgumentException("Bean can not be resolved" );
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
