package de.mq.mapping.util.proxy;


public interface  Action  {
	Object execute(final Class<?> result, final String bundle, final ModelRepository modelRepository) throws Exception;
    
}
