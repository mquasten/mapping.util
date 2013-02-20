package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import de.mq.mapping.util.proxy.BeanResolver;

import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistController;

public class SpringBeanResolverTest {
	
	@Test
	public final void beansOfType() {
		final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		final BeanResolver beanResolver = new SpringBeanResolverImpl(applicationContext);
		final ArtistController artistController = Mockito.mock(ArtistController.class);
		Mockito.when(applicationContext.getBean(ArtistController.class)).thenReturn(artistController);
		
		Assert.assertEquals(artistController, beanResolver.getBeanOfType(ArtistController.class));
	}
	
	@Test
	public final void put() {
		final ArtistController artistController = Mockito.mock(ArtistController.class);
		final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		final BeanResolver beanResolver = new SpringBeanResolverImpl(applicationContext);
		beanResolver.put(Artist.class, artistController);
		
		Mockito.verifyZeroInteractions(applicationContext);
	}

}
