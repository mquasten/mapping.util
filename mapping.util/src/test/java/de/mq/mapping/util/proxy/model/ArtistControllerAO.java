package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;

public abstract class ArtistControllerAO {
	
	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent(params={@Parameter(clazz=ArtistAO.class, el="#arg.artist", elResultType=Artist.class)})})
	public abstract void store();
	
	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent( name="store" , params={@Parameter(clazz=ArtistAO.class )})})
	public abstract void store2();
	
	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent( name="store" , params={@Parameter(clazz=Artist.class , domain=ArtistImpl.class )})})
	public abstract void storeDomain();
	
	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent( name="store" , params={@Parameter(clazz=Artist.class , property="artist" )})})
	public abstract void storeProperty();
	
	@MethodInvocation(clazz=ArtistControllerImpl.class)
	public abstract void dummy();
	
	public abstract void dummy2();
	
	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent( endConversation=true, startConversation=true ) })
	public abstract void conversation() ;

}
