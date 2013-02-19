package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;

public class AbstractBeanConventionProxyFactoryTest {
	
	@Test
	public final void create() {
		final AbstractBeanConventionProxyFactory factory = new FactoryMock();
		
		Assert.assertEquals(7, ((Constructor[])ReflectionTestUtils.getField(factory, "interceptors")).length);
		Assert.assertEquals(7, ((Map<?,?>)ReflectionTestUtils.getField(factory, "indexes")).size());
		Assert.assertEquals(7, factory.numberOfInterceptors());
	}
	
	@Test
	public final void index() {
		
		
		final AbstractBeanConventionProxyFactory factory = new FactoryMock();
		Assert.assertEquals(0, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getName"), -1));
		
		Assert.assertEquals(1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getVideos"), -1));
		
		Assert.assertEquals(2, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getDuetPartner"), -1));
		Assert.assertEquals(3, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getArtist"), -1));
		Assert.assertEquals(4, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "setName", String.class), -1));
		Assert.assertEquals(5, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "setArtist", Artist.class), -1));
		
		Assert.assertEquals(19680528, (int) factory.index(ReflectionUtils.findMethod(GregorianCalendar.class, "getTime"), 19680528));
		
		((Map<?,?>)ReflectionTestUtils.getField(factory, "indexes")).clear();
		Assert.assertEquals(-1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getName"), -1));
		
		Assert.assertEquals(-1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getVideos"), -1));
		
		Assert.assertEquals(-1,  (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getDuetPartner"), -1));
		Assert.assertEquals(-1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "getArtist"), -1));
		Assert.assertEquals(-1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "setName", String.class), -1));
		Assert.assertEquals(-1, (int) factory.index(ReflectionUtils.findMethod(ArtistAO.class, "setArtist", Artist.class), -1));
	}
	
	
	@Test
	public final void interceptors() {
		final AbstractBeanConventionProxyFactory factory = new FactoryMock();
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		@SuppressWarnings("unchecked")
		final Constructor<? extends Interceptor>[]  interceptors = (Constructor<? extends Interceptor>[]) ReflectionTestUtils.getField(factory, "interceptors");
		for(int i =0; i < factory.numberOfInterceptors(); i++){
			Assert.assertEquals(interceptors[i].getDeclaringClass(), factory.interceptor(i, modelRepository).getClass());
		}
		
		
	}
	@Test(expected=IllegalArgumentException.class)
	public final void interceptorsNoSuchMethod() throws NoSuchMethodException, SecurityException {
		final AbstractBeanConventionProxyFactory factory = new FactoryMock();
		@SuppressWarnings("unchecked")
		final Constructor<? extends Interceptor>[]  interceptors = (Constructor<? extends Interceptor>[]) ReflectionTestUtils.getField(factory, "interceptors");
		interceptors[0]=InterceptorMock.class.getDeclaredConstructor();
		factory.interceptor(0, Mockito.mock(ModelRepository.class));
		
	}
	
	@Test(expected=IllegalStateException.class)
	public final void interceptorsBadConstructor() {
		final AbstractBeanConventionProxyFactory factory = new FactoryMock();
		((Class<?>[]) ReflectionTestUtils.getField(factory, "classes"))[0]=InterceptorMock.class;
		factory.interseptors();
	}
	
	

	
	@Test
	public final void callWithModelRepository() {
		
		final AbstractBeanConventionProxyFactory factory = Mockito.mock(AbstractBeanConventionProxyFactory.class);
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		
		factory.createProxy(ArtistAO.class,   modelRepository );
		
		Mockito.verify(factory).createProxy(ArtistAO.class, modelRepository);
		
	}
	
	
	
	

}

class FactoryMock extends AbstractBeanConventionProxyFactory {

	@Override
	public <T> T createProxy(Class<? extends T> targetClass, ModelRepository modelRepository) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class InterceptorMock implements Interceptor {
	
	

	@Override
	public Object invoke(Method method, Object[] args) throws Throwable {
		
		return null;
	}
	
}