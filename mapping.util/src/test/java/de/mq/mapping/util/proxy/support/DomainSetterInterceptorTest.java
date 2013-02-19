package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;


public class DomainSetterInterceptorTest {
	
	@Test
	public final void interceptor() throws Throwable {
		final ModelRepository  modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor interceptor = new DomainSetterInterceptorImpl (modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "setArtist", Artist.class);
		Artist artist = Mockito.mock(Artist.class);
		Assert.assertNull(interceptor.invoke(method, new Object[] { artist}));
		
		Mockito.verify(modelRepository).put(artist);
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void invokeWrongArgument() throws Throwable {
		final ModelRepository  modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor interceptor = new DomainSetterInterceptorImpl (modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "setArtist", Artist.class);
		interceptor.invoke(method, new Object[] { });
		
	}
	
	

}
