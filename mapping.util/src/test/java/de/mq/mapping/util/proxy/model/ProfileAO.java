package de.mq.mapping.util.proxy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;

/* in memorial ... */
/**
 * The profile of an artist. Example for a map model
 * @author Admin
 *
 */
public abstract  class ProfileAO  {
	
	@Getter("url" )
	public abstract String getUrl();
	
	@Setter("url")
	public abstract void setUrl(final String url) ;
	
	
	@Getter("id" )
	public abstract Long getId();
	
	@Setter("id")
	public abstract void setId(final Long url) ;
	
	
	@GetterDomain()
	public abstract Map<String,Object> getMap();
	
	@SetterDomain()
	public abstract void setMap(final Map<String,Object> map);
	
	@GetterProxy(name="artist" ,proxyClass=ArtistAO.class)
	public abstract ArtistAO getArtist();
	
	@GetterProxyCollection(collectionClass=ArrayList.class,name="friends",proxyClass=ArtistAO.class)
	public abstract Collection<ArtistAO> getFriends();

}
