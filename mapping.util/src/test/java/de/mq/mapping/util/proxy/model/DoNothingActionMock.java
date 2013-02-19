package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ModelRepository;

public class DoNothingActionMock  implements Action{

	

	
	@Override
	public Object execute(Class<?> result, String bundle,  final ModelRepository modelRepository) throws Exception {
		
		return modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class).createProxy(ArtistAO.class, modelRepository);
		
	}

}
