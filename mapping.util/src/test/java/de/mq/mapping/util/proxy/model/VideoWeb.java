package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.Getter;

public interface VideoWeb {
	
	@Getter(value="title",clazz=VideoImpl.class)
	String getTitle();
	
	@Getter(value="id", clazz=VideoImpl.class)
	Long getId();

}
