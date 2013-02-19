package de.mq.mapping.util.proxy.model;

import org.mockito.Mockito;

import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;


public class ArtistControllerImpl  implements ArtistController {
	
	private final BeanConventionCGLIBProxyFactory factory = new BeanConventionCGLIBProxyFactory();
	
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.model.ArtistController#artist(java.lang.Long)
	 */
	@Override
	@ExceptionTranslations( clazz=ArtistControllerImpl.class,value={@ExceptionTranslation( source=IllegalArgumentException.class, action = ActionMock.class, bundle="artist_not_found" )})
	public ArtistAO artist(final Long id )   {
		final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
		if( id < 0) {
			
			throw new IllegalArgumentException("Artist not found id: " + id);
		}
		
		if( id==0L ) {
			throw new RuntimeException();
		}
        final ModelRepository modelRepository =  new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(new ArtistImpl("Kylie", 10)).build();
        		
		return factory.createProxy(ArtistAO.class, modelRepository);
		
	}
	

	@ExceptionTranslations( clazz=ArtistControllerImpl.class,value={@ExceptionTranslation( source=IllegalArgumentException.class, action = Action.class, bundle="artist_not_found" )})
	public ArtistAO artist()   {
		throw new IllegalArgumentException();
	}
	
	@ExceptionTranslations( clazz=ArtistControllerImpl.class,value={@ExceptionTranslation( source=IllegalArgumentException.class, action = DoNothingActionMock.class, bundle="artist_not_found" )})
	public ArtistAO artistFacesMessage()   {
		throw new IllegalArgumentException();
	}
	
	

}