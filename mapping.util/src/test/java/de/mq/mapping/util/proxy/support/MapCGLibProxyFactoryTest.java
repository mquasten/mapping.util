package de.mq.mapping.util.proxy.support;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.model.ProfileAO;

public class MapCGLibProxyFactoryTest {
	
	private  BeanResolver beanResolver;
	
	private Converter<Object,Object> noConverter = new NoConverter();
	
	@Before
	public final void setup() {
		beanResolver = Mockito.mock(BeanResolver.class);
		Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn((NoConverter) noConverter);
	}
	
	
	@Test
	public final void testGetter() {		 
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("url", "www.kylie.com");
		
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final ProfileAO plotAO = domainProxyFacory.createProxy(ProfileAO.class , modelRepository);
		
		
		Assert.assertEquals(map.get("url"), plotAO.getUrl());

		
	}


	
	@Test
	public final void testGetterNotSet() {
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		Assert.assertNull(profileAO.getUrl());
	}
	
	
	
	@Test
	public final void testGetterLong() {
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		Assert.assertNull(profileAO.getId());
		
		final Map<Key,Object> items=getModelItems(modelRepository);
		items.put(new KeyImpl("id"), 19680528L);
		
		Assert.assertEquals(Long.valueOf(19680528L), profileAO.getId());
	}




	@SuppressWarnings("unchecked")
	private Map<Key, Object> getModelItems(final ModelRepository modelRepository) {
		return (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
	}
	

	@Test
	public final void testSetterLong() {
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		profileAO.setId(19680528L);
		Assert.assertEquals(Long.valueOf(19680528L),getModelItems(modelRepository).get(new KeyImpl("id")));
	}
	
	
	@Test
	public final void testSetter() {
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		final ProfileAO plotAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		plotAO.setUrl("www.kylie.com");
		
		Assert.assertEquals(1, getModelItems(modelRepository).size());
		Assert.assertEquals("www.kylie.com", getModelItems(modelRepository).get(new KeyImpl("url")));
		
		plotAO.setUrl("www.pcd-music.com");
		Assert.assertEquals("www.pcd-music.com", getModelItems(modelRepository).get(new KeyImpl("url")));
		
		
	}
	
	
	
	@Test
	public final void testGetterDomain() {
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("url", "www.kylie.com");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		
		
		Assert.assertEquals(map, profileAO.getMap());
		
	}
	
	
	
	@Test
	public final void testSetterDomain() {
	
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		
		Assert.assertEquals(0, profileAO.getMap().size());
		
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("url", "www.kylie.com");
		profileAO.setMap(map);
		Assert.assertEquals(map, profileAO.getMap());
		
	}
	
	
	
	@Test
	public final void testGetterProxy() {
		final Artist artist = new ArtistImpl("Kylie", 10);
	
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("artist", artist);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(domainProxyFacory);
		
		
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		
		Assert.assertEquals(ArtistAO.class, profileAO.getArtist().getClass().getSuperclass());
		
		Assert.assertTrue(profileAO.getArtist().getClass().getSimpleName().startsWith("ArtistAO$$EnhancerByCGLIB$$"));
		
		Assert.assertEquals(artist.name(), profileAO.getArtist().getName());
		Assert.assertEquals(artist.hotScore(), profileAO.getArtist().gethotScore());
		
	}
	
	
	@Test
	public final void testGetterCollectionProxy() {

		
		final AOProxyFactory domainProxyFacory = new BeanConventionCGLIBProxyFactory();
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(domainProxyFacory);
		
		final Map<String,Object> map = new HashMap<String,Object>();
	
		final List<Artist> friends = new ArrayList<Artist>();
		friends.add(new ArtistImpl("Jason Donovan" ));
		friends.add(new ArtistImpl("Michael Hutchence"));
		map.put("friends", friends);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		final ProfileAO profileAO = domainProxyFacory.createProxy(ProfileAO.class, modelRepository);
		
		Assert.assertEquals(ArrayList.class, profileAO.getFriends().getClass());
		Assert.assertEquals(2, profileAO.getFriends().size());
		int i=1; 
		for(final ArtistAO artistAO : profileAO.getFriends() ){
			Assert.assertEquals(ArtistAO.class, artistAO.getClass().getSuperclass());
			Assert.assertEquals(friends.get(i-1).name() , artistAO.getName());
			Assert.assertEquals(friends.get(i-1), artistAO.getArtist());
			i++;
		}
		
	}
	
	

}
