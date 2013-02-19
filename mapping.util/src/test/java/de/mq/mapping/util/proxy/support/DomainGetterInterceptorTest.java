package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;

public class DomainGetterInterceptorTest {
	
	@Test
	public final void invoke() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Artist artist = Mockito.mock(Artist.class);
		Mockito.when(modelRepository.get(ArtistImpl.class)).thenReturn(artist);
		final Interceptor interceptor = new DomainGetterInterceptorImpl(modelRepository); 
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getArtist");
		Assert.assertEquals(artist, interceptor.invoke(method, new Object[] { }));
		Mockito.verify(modelRepository).get(ArtistImpl.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public final void invokeMissingAnnotation() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor interceptor = new DomainGetterInterceptorImpl(modelRepository); 
		final Method method = ReflectionUtils.findMethod(GregorianCalendar.class, "getTime");
		interceptor.invoke(method, new Object[] { });
		
		
	}

}
