package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.model.ArtistSelector;
import de.mq.mapping.util.proxy.model.HotArtistAO;
import de.mq.mapping.util.proxy.model.HotArtistImpl;

public class BasicGetterProxyInterceptorTest {
	
	private BeanResolver beanResolver;
	private ModelRepository modelRepository;
	private AOProxyFactory proxyFactory;
	private Converter<Object,Object> noConverter = new NoConverter();
	
	
	@Before
	public final void setup() {
		modelRepository = Mockito.mock(ModelRepository.class);
		beanResolver = Mockito.mock(BeanResolver.class);
		proxyFactory = Mockito.mock(AOProxyFactory.class);
		Mockito.when(modelRepository.beanResolver()).thenReturn(beanResolver);
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(proxyFactory);
		Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn((NoConverter) noConverter);
	}

	@Test
	@SuppressWarnings("unchecked")
	public final void intercept() throws Throwable {
		final Artist artist = Mockito.mock(Artist.class);
		Mockito.when(modelRepository.get(ArtistImpl.class, "duetPartner")).thenReturn(artist);
		final Interceptor interceptor = new BasicGetterProxyInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getDuetPartner");
		final ArtistAO duetAO = Mockito.mock(ArtistAO.class);

		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class), Mockito.any(ModelRepository.class))).thenReturn(duetAO);
		
		Assert.assertEquals(duetAO, interceptor.invoke(method, new Object[] { "duetPartner" }));
		Mockito.verify(modelRepository).get(ArtistImpl.class, "duetPartner");
	}

	@Test
	public final void invokeNullFromRepository() throws Throwable {
		Mockito.when(modelRepository.get(ArtistImpl.class, "duetPartner")).thenReturn(null);
		final Interceptor interceptor = new BasicGetterProxyInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getDuetPartner");

		Assert.assertNull(interceptor.invoke(method, new Object[] { "duetPartner" }));
		Mockito.verify(modelRepository).get(ArtistImpl.class, "duetPartner");
		Mockito.verifyZeroInteractions(proxyFactory);
	}

	@Test(expected = IllegalStateException.class)
	public final void invokeMissingAnnotation() throws Throwable {
		final Interceptor interceptor = new BasicGetterProxyInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(GregorianCalendar.class, "getTime");
		interceptor.invoke(method, new Object[] { "time" });
	}
	
	
	@Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public final void converter() throws Throwable {
            final ArtistSelector artistSelector = new ArtistSelector();
            Mockito.when(beanResolver.getBeanOfType(ArtistSelector.class)).thenReturn(artistSelector);
		
            final HotArtistAO hotArtistAO = Mockito.mock(HotArtistAO.class);
            final ArgumentCaptor<ModelRepository> repoCaptor = ArgumentCaptor.forClass(ModelRepository.class);
			final ArgumentCaptor<Class> clazzCaptor = ArgumentCaptor.forClass(Class.class);;
            final Artist artist = new HotArtistImpl("kylie", 500e3);
            Mockito.when(modelRepository.get(ArtistImpl.class, "duetPartner")).thenReturn(artist);
            final Interceptor interceptor = new BasicGetterProxyInterceptorImpl(modelRepository);
            final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getDuetPartner2");
    
            Mockito.when(proxyFactory.createProxy(clazzCaptor.capture(), repoCaptor.capture())).thenReturn(hotArtistAO);
            
            Assert.assertEquals(hotArtistAO, interceptor.invoke(method, new Object[] { "duetPartner" }));
    
            Assert.assertEquals(ModelRepositoryImpl.class, repoCaptor.getValue().getClass());
            Assert.assertEquals(HotArtistAO.class, clazzCaptor.getValue());
            
            
    }


}
