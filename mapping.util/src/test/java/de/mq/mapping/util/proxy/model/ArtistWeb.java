package de.mq.mapping.util.proxy.model;

import java.util.ArrayList;
import java.util.List;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;

/**
 * The (web) model of an artist
 * @author ManfredQuasten
 *
 */
public interface ArtistWeb {
	
	
	@Getter(value="id" , clazz=ArtistImpl.class)
	Long getId() ; 
	
	
	@Getter(value="hotScore" , clazz=ArtistImpl.class)
	Integer gethotScore() ; 
	
	
	@Getter(value="name" , clazz=ArtistImpl.class)
	String getName() ;
	
	
	@Setter(value="name" , clazz=ArtistImpl.class )
	void setName(final String name);
	
	
	@GetterDomain(clazz=ArtistImpl.class)
	Artist getArtist();
	
	
	@SetterDomain(clazz=ArtistImpl.class)
	void setArtist(final Artist artist) ;
	
	
	@Setter(value="hotScore", clazz=ArtistImpl.class)
	void setHotScore(final Integer hotScore) ;
	
	@GetterProxyCollection(clazz=ArtistImpl.class, name="videos" ,proxyClass=VideoWeb.class,collectionClass=ArrayList.class, comparator=VideoWebComparator.class)
	List<VideoWeb> getVideos();
	
	@GetterProxy(clazz=ArtistImpl.class, name="duetPartner", proxyClass=ArtistWeb.class)
	ArtistWeb getDuetPartner();


}
