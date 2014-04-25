package de.mq.mapping.util.proxy.model;

import java.util.ArrayList;
import java.util.List;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;
import de.mq.mapping.util.proxy.support.Number2StringConverter;
import de.mq.mapping.util.proxy.support.String2IntegerConverter;

/**
 * An abstract class that represents the (web) model of an artist
 * @author ManfredQuasten
 *
 */
public abstract class ArtistAO {
	
	@Getter(value="id" , clazz=ArtistImpl.class)
	public abstract Long getId() ; 
	
	@Getter(value="hotScore" , clazz=ArtistImpl.class)
	public abstract Integer gethotScore() ; 
	
	
	@Getter(value="hotScore" , clazz=ArtistImpl.class, converter=Number2StringConverter.class)
	public abstract String gethotScoreAsString() ; 
	
	
	
	@Getter(value="name" , clazz=ArtistImpl.class)
	public abstract String getName() ;
	
	
	@Setter(value="name" , clazz=ArtistImpl.class )
	public abstract void setName(final String name);
	
	@SetterDomain(clazz=ArtistImpl.class)
	public abstract void setArtist(final Artist artist) ;
	
	
	@Setter(value="hotScore", clazz=ArtistImpl.class)
	public abstract void setHotScore(final Integer hotScore) ;
	
	@Setter(value="hotScore", clazz=ArtistImpl.class , converter=String2IntegerConverter.class)
	public abstract void setHotScoreAsString(final String hotScore) ;
	
	@GetterProxyCollection(clazz=ArtistImpl.class, name="videos" ,proxyClass=VideoAO.class,collectionClass=ArrayList.class, comparator=VideoAOComparator.class)
	public abstract  List<VideoAO> getVideos();

	@GetterProxy(clazz=ArtistImpl.class, name="duetPartner", proxyClass=ArtistAO.class)
	public abstract ArtistAO getDuetPartner();
	
	@GetterDomain(clazz=ArtistImpl.class)
	public abstract Artist getArtist();
	
	@Getter(clazz=ArtistImpl.class, value = "")
	public abstract String getArtist2() ;
	
	

	@GetterProxy(clazz=ArtistImpl.class, name="duetPartner", proxyClass=ArtistSelector.class)
	public abstract ArtistAO getDuetPartner2();
	
	@MethodInvocation(actions={@ActionEvent(clazz=ArtistControllerImpl.class , params={@Parameter(clazz=ArtistAO.class,proxy=true)})}, clazz = ArtistImpl.class )
	public abstract void init();
	

}
