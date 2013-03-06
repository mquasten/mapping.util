package de.mq.mapping.util.proxy;


public interface  Action  {
	Object execute(final ExceptionTranslation exceptionTranslation, final ModelRepository modelRepository, final Throwable ex, final Object[] args) throws Exception;
	
}
