package de.mq.mapping.util.proxy;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

/**
 * DataStore to hold values , domainObjects, fields in domainObjects that contains invalid values, 
 * temporary values that should not be written to domainObjects, cached proxies and cached collections
 * 
 * @author ManfredQuasten
 *
 */
public interface ModelRepository {

	/**
	 * Adds or replace an existing domainObject to the store
	 * @param value the domainObject, that should be aaded or replaced
	 */
	void put(final Object value);

	/**
	 * Add or replace field values for domainObjects or temporary values to the store
	 * @param clazz the domainClass in case of a fieldValue,  or NoModel.class in case of a temporary value, that should not be written to the domain
	 * @param field the name of the field or the name under that the value should be stored in the map.
	 * @param value the value, that should be added or replaced
	 * @param converter a converter that should be executed, before the value is written to the domaninObject
	 */
	void put(final Class<?> clazz, final String field, final Object value,@SuppressWarnings("rawtypes") final Class<? extends Converter> converter);

	/**
	 * Put a value to the cache. Proxies or collections of Proxies must be cached, that the temporary values will not be lost
	 * @param clazz thedomainClass where the cached vales hanged up 
	 * @param uuid the unique id for the proxy  or the collection
	 * @param value the value that should be stored
	 */
	void put(final Class<?> clazz, final UUID uuid, final Object value);
	
	/**
	 * Gets a stored domainObject from the repository
	 * @param clazz the class of the domainObject, attention with hibernateProxies
	 * @return the value that is stored
	 */
	Object get(final Class<?> clazz);
	
	
	/**
	 * Gets the value that is stored for a domainField, or a temporary value for NOModel.class
	 * @param clazz the class of the domainObject, or NoModel.class, if it is a temporary value
	 * @param name the name of the field or the name under that the value is stored
	 * @return the stored value
	 */
	Object get(final Class<?> clazz, final String name);
	
	/**
	 * Gets the value that is stored for a domainField, or a temporary value for NOModel.class. 
	 * If the result isn't matching to the given ResultType, the converter is used to convert it.
	 * @param clazz the class of the domainObject, or NoModel.class, if it is a temporary value
	 * @param name the name of the field or the name under that the value is stored
	 * @param converter the type of the converter that should be used, to convert the result, before it is returned
	 * @param resultType If the ResultType isn't matching to the result that is stored, the converter is executed.
	 * @return the stored value
	 */
	Object get(final Class<?> clazz, final String name,  @SuppressWarnings("rawtypes") final Class<? extends Converter>  converter, final Class<?> resultType );
	/**
	 * Get the value for a stored proxy or a stored Collection of proxies
	 * @param clazz thedomainClass where the cached vales hanged up 
	 * @param uuid the unique id of the stored proxy / collection
	 * @return the value that is stored
	 */
	Object get(final Class<?> clazz, final UUID uuid);
	
	/**
	 * Exists an error for the field, for example, because the Type for the field wrong
	 * @param clazz the class of the domainObject, or NoModel.class for a temporary value
	 * @param field the name of the field or the name under that the temporary value has been stored in the map
	 * @return true if an error exists else false
	 */
	boolean hasError(final Class<?> clazz, final String field);
	
	/**
	 * Is a value stored or the given UUID for the Proxy or the Collection from the DomainOjectClass
	 * @param clazz the domainClass where the cached vales hanged up 
	 * @param uuid the unique id of the Proxy or the collection
	 * @param domainCollection for a collection the size of the cached collection must be identical, 
	 *        otherwise it is not use able. It is removed from cache
	 * @return true if a value is stored, else null
	 */
	boolean isCached(final Class<?> clazz, final UUID uuid, final Object domainCollection ); 
	
	/**
	 * The beanResolber, strategy how beens will be gotten, string, reflection, whatever
	 * @return the Resolver Interface, for beans
	 */
	BeanResolver beanResolver();
	
	/**
	 * Clear the Repository or clear part of it.  If no class is given the complete repository is removed
	 * @param domainClasses all items , the domainClass and all its fields, errors and proxies will be removed
	 */
    void clear(final Class<?> ... domainClasses);

}