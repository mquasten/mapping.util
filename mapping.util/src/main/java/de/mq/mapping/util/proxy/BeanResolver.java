package de.mq.mapping.util.proxy;

/**
 * Strategy interface for resolving beans.
 * Beans can be created using reflection, cached after that in Map. 
 * Otherwise beans can be resoved by a ioc container like spring
 * @author Admin
 *
 */
public interface BeanResolver {
	
	/**
	 * Gets the bean for the given type
	 * @param clazz the type of the bean
	 * @return the bean, when it can be resolved or created, otherwise a RuntimeException from the container 
	 * or from the specific implementation of the Resolver is thrown.  
	 */
	<T> T getBeanOfType(Class<? extends T> clazz);
	
	/**
	 * Add or replace bean for the given type.
	 * @param clazz the type that is used, to resolve the bean
	 * @param bean the instance of the bean for the related type
	 */
	void put(final Class<?> clazz, final Object bean);
}
