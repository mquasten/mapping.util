package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;

public class BasicGetterInterceptorTest {
	
	private static final String VALUE = "Kylie";

	@Test
	public final void invoke() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		Mockito.when(modelRepository.get(ArtistImpl.class, "name", NoConverter.class, String.class)).thenReturn(VALUE);
		Interceptor interceptor = new BasicGetterInterceptorImpl(modelRepository); 
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getName");
		Assert.assertEquals(VALUE, interceptor.invoke(method, new Object[] { }));
	}
	
	
	@Test
	public final void invokeGetDomainObjectWithConverter() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		Mockito.when(modelRepository.get(ArtistImpl.class, null, NoConverter.class,  String.class)).thenReturn(VALUE);
		Interceptor interceptor = new BasicGetterInterceptorImpl(modelRepository); 
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getArtist2");
		Assert.assertEquals(VALUE, interceptor.invoke(method, new Object[] { }));
	}
	
	@Test(expected=IllegalStateException.class)
	public final void missingAnnotation() throws Throwable{
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Method method = ReflectionUtils.findMethod(GregorianCalendar.class, "getTime");
		final Interceptor interceptor = new BasicGetterInterceptorImpl(modelRepository); 
		interceptor.invoke(method, new Object[] { });
	}

}
