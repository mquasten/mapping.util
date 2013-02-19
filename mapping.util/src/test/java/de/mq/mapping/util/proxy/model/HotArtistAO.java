package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;

public abstract  class HotArtistAO {
	
	@Getter(value="price" , clazz=HotArtistAO.class)
	public abstract Double getPrice() ;
	
	
	@Getter(value="id" , clazz=HotArtistImpl.class)
	public abstract Long getId() ; 
	
	@Getter(value="hotScore" , clazz=HotArtistImpl.class)
	public abstract Integer gethotScore() ; 
	
	
	
	
	
	
	@Getter(value="name" , clazz=HotArtistImpl.class)
	public abstract String getName() ;
	
	
	@Setter(value="name" , clazz=HotArtistImpl.class )
	public abstract void setName(final String name);
	
	@SetterDomain(clazz=HotArtistImpl.class)
	public abstract void setArtist(final Artist artist) ;
	
	
	@Setter(value="hotScore", clazz=HotArtistImpl.class)
	public abstract void setHotScore(final Integer hotScore) ;
	
	
	@GetterDomain(clazz=ArtistImpl.class)
	public abstract HotArtistImpl getArtist();

}
