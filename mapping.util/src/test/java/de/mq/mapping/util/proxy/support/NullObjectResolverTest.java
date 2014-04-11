package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.NullObjectResolver;



public class NullObjectResolverTest {
	
	@Test
	public final void add() {
		final Artist artist = Mockito.mock(Artist.class);
		Mockito.when(artist.name()).thenReturn("Kylie Ann Minoge");
		final BasicNullObjectResolverImpl  resolver = new BasicNullObjectResolverImpl(); 
		resolver.put(Artist.class, artist);
		resolver.put(artist.getClass(), artist);
		
		Assert.assertEquals(artist, resolver.forType(Artist.class));
		Assert.assertEquals(artist, resolver.forType(artist.getClass()));
	}
	
	
	@Test
	public final void forTypeNull() {
		final NullObjectResolver  resolver = new BasicNullObjectResolverImpl(); 
		Assert.assertNull(resolver.forType(Artist.class));
	}
	
	
	interface  Artist {
		String name();
	}

}

