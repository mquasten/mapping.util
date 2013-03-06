package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ModelRepository;

public class DoNothingActionMock  implements Action{

	@Override
	public Object execute(ExceptionTranslation exceptionTranslation, final ModelRepository modelRepository, final Throwable ex, final Object[] args) throws Exception {
		return modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class).createProxy(ArtistAO.class, modelRepository);
		
	}

}
