package de.mq.mapping.util.proxy.support;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistImpl;

public class SimpleReflectionBeanResolverTest {
	
	@Test
	public final void putAndGet() {
		final Artist artist = Mockito.mock(Artist.class);
		final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
		beanResolver.put(Artist.class, artist);
		Assert.assertEquals(artist, beanResolver.getBeanOfType(Artist.class));
	}
	
	@Test
	public final void getVirgin() {
		final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
		
		final Artist result = beanResolver.getBeanOfType(ArtistImpl.class);
		
		Assert.assertEquals(ArtistImpl.class, result.getClass());
		for(int i=0; i < 10; i++){
			Assert.assertEquals(result, beanResolver.getBeanOfType(ArtistImpl.class));
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public final void unableToCreate() {
		final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
		beanResolver.getBeanOfType(Artist.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void typeGuard() {
		final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
		beanResolver.put(Artist.class, new Date());
	}

}
