package de.mq.mapping.util.proxy.support;

interface Key {

	boolean hasParent(final Class<?> clazz);
	
	boolean isMapKey(); 
	
	
	String name();

}