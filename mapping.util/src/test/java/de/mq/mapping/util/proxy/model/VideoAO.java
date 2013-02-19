package de.mq.mapping.util.proxy.model;

import java.util.ArrayList;
import java.util.List;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterProxyCollection;

public abstract class VideoAO {
	
	@Getter(value="title",clazz=VideoImpl.class)
	public abstract String getTitle();
	
	@Getter(value="id", clazz=VideoImpl.class)
	public abstract Long getId();
	
	@GetterProxyCollection(clazz=VideoImpl.class, proxyClass=ArtistSelector.class ,collectionClass=ArrayList.class,name="artists")
	public abstract List<?> getArtists();

}
