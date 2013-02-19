package de.mq.mapping.util.proxy.model;

import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;

public interface ArtistController {

	@ExceptionTranslations(clazz=ArtistControllerImpl.class,value={@ExceptionTranslation(source = IllegalArgumentException.class, action = ActionMock.class, bundle = "artist_not_found")})
	public abstract ArtistAO artist(Long id) throws Exception;

}