package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ModelRepository;

public class ActionMock implements Action  {

	
	
	@Override
	public Object execute(final Class<?> result, final String bundle, final ModelRepository modelRepository) throws Exception {
		throw new ClassNotFoundException(bundle);
	}


	
	

}
