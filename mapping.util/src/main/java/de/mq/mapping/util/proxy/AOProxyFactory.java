package de.mq.mapping.util.proxy;



/**
 * Create a proxy to assuage sun's stupid bean convention
 * This convention is incompatible with domain driven design and object oriented programming 
 * (encapsulation, exhibitionism of objects)
 * Sun's / oracles frameworks (like JSF) ignore DDD. They needs getters and setters.
 * This Factory will create proxies to for getters and setters on existing domain object
 * 
 * @author ManfredQuasten
 *
 * @param <Web> the web model, the model with the generated getters and setters
 */
public interface AOProxyFactory {
	
    
	/**
	 * Creates a proxy for the given class and the given Repository
	 * @param targetClass the class for which the proxy should be created
	 * @param modelRepository the modelRepository within the objects
	 * @return the created proxy
	 */
	<Web> Web createProxy(final Class<? extends Web> targetClass, final ModelRepository  modelRepository);
	
}