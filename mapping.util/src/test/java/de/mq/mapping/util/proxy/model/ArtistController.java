package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.MethodInvocation;

public interface ArtistController {

	@MethodInvocation(clazz=ArtistControllerImpl.class,value={@ExceptionTranslation(source = IllegalArgumentException.class, action = ActionMock.class, bundle = "artist_not_found")})
	public abstract ArtistAO artist(Long id) throws Exception;

}