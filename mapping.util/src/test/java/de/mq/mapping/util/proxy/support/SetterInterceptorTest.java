package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;


public class SetterInterceptorTest {
	
	@Test
	public final void invake() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor  interceptor = new SetterInterceptorImpl(modelRepository);
		final Method method =  ReflectionUtils.findMethod(ArtistAO.class, "setName", String.class);
		Assert.assertNull(interceptor.invoke(method, new Object[] { "Kylie"} ));
		
		Mockito.verify(modelRepository).put(ArtistImpl.class, "name", "Kylie", NoConverter.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public final void invokeMissingAnnotation() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor  interceptor = new SetterInterceptorImpl(modelRepository);
		
		final Method method =  ReflectionUtils.findMethod(GregorianCalendar.class, "setTime", Date.class);
		interceptor.invoke(method, new Object[] { new Date()} );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void invokeWrongArguments() throws Throwable {
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final Interceptor  interceptor = new SetterInterceptorImpl(modelRepository);
		
		final Method method =  ReflectionUtils.findMethod(ArtistAO.class, "setName", String.class);
		
		interceptor.invoke(method, new Object[] {} );
		
	}

}
