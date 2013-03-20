package de.mq.mapping.util.proxy;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.VideoImpl;

public class ProxyUtilTest {
	
	
	@Test
	public final void collectDomains() {
		
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
		final Artist artist = Mockito.mock(Artist.class);
		Mockito.when(artistAO.getArtist()).thenReturn(artist);
		
		@SuppressWarnings("unchecked")
		final Set<Artist> artists = (Set<Artist>) ProxyUtil.collectDomains(artist.getClass(), artistAO);
		Assert.assertEquals(1, artists.size());
		
	}
	
	@Test
	public final void collectDomainsTypeNotMatching() {
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
		final Artist artist = Mockito.mock(Artist.class);
		Mockito.when(artistAO.getArtist()).thenReturn(artist);
		
		Assert.assertTrue(ProxyUtil.collectDomains(VideoImpl.class, artistAO).isEmpty());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void errorGettingDomainObject() {
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
		
		Mockito.when(artistAO.getArtist()).thenThrow(new RuntimeException("Don't worry it's for test only"));
		
		Assert.assertTrue(ProxyUtil.collectDomains(VideoImpl.class, artistAO).isEmpty());
	}
	
	@Test
	public final void coverageOnly() {
		Assert.assertNotNull(new ProxyUtil());
	}
	

}
