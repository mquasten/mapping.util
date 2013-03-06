package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ModelRepository;

public class ActionMock implements Action  {

	
	
	@Override
	public Object execute(ExceptionTranslation exceptionTranslation, final ModelRepository modelRepository, Throwable ex, Object[] args ) throws Exception {
		throw new ClassNotFoundException(exceptionTranslation.bundle());
	}


	
	

}
