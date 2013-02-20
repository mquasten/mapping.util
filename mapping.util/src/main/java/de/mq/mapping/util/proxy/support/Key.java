package de.mq.mapping.util.proxy.support;

/**
 * The Key in a map for the entries in the repository.
 * This key is used, to differ between domainClasses, attributes, errors  
 * @author MQuasten
 *
 */
interface Key {

	/**
	 * Is the given lass is a child from an other.
	 * @param clazz the class that should be checked
	 * @return true if it has a parent, it is an child else false
	 */
	boolean hasParent(final Class<?> clazz);
	
	/**
	 * The key isn't written, read from a domainClass. It is simply chached in a map.
	 * @return true if non domainObject is used
	 */
	boolean isMapKey(); 

	/**
	 * The name of the key
	 * @return the name from key
	 */
	String name();

}