package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.ModelRepository;
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
		   final Class<?> clazz =  method.getAnnotation(MethodInvocation.class).clazz();
		   
		   Object result=null; 
		   boolean likeAVirgin=true;
		   for(final ActionEvent action : method.getAnnotation(MethodInvocation.class).actions()){
			   final Object methodResult = handleActionEvent(method, args, clazz, action);
			   if( methodResult != null){
				   result=methodResult;
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

	private Object handleActionEvent(final Method method, final Object[] args, final Class<?> clazz, final ActionEvent action) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		final List<Class<?>> paramClasses = new ArrayList<>();
		final List<Object> paramValues = new ArrayList<>();
		for (final Parameter parameter : action.params()) {
			paramClasses.add(parameterClass(parameter)); 
			paramValues.add(handleParameterValue(args,parameter));
		}
		final Method targetMethod = clazz.getMethod(methodName(method, action), paramClasses.toArray(new Class[paramClasses.size()]));		
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

	private Object handleParameterValue(final Object[] args, final Parameter parameter) {
	    if( parameter.originIndex() >=0 ){
			return args[parameter.originIndex()];
			 
		}
		final Object managedBean = modelRepository.beanResolver().getBeanOfType(parameter.clazz());
		if( managedBean==null){
			throw new IllegalArgumentException("Bean of type " + parameter.clazz() + " can not be resolved" );
		}
		
		if( parameter.el().trim().length() == 0 ){
			   return managedBean ;   
		} 
		return parseEl(parameter.el(), managedBean, ARG);
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
	
	private Object parseEl(final String expression, final Object arg, final String argName) {
		final ExpressionParser parser = new SpelExpressionParser();
		final StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable(argName, arg);
		return parser.parseExpression(expression).getValue(context);
	}

}
