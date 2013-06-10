package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;

public interface ArtistController {

	@MethodInvocation(clazz=ArtistControllerImpl.class, actions={ @ActionEvent(params={@Parameter(clazz=Long.class, originIndex=0 )})}, value={@ExceptionTranslation(source = IllegalArgumentException.class, action = ActionMock.class, bundle = "artist_not_found")})
	public abstract ArtistAO artist(Long id) throws Exception;
	
	

}