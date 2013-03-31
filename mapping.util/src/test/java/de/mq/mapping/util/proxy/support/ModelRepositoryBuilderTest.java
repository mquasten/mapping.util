package de.mq.mapping.util.proxy.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.Artist;

public class ModelRepositoryBuilderTest {
	
	@Test
	public  final void withResolver() {
		final ModelRepositoryBuilder modelRepositoryBuilder = new ModelRepositoryBuilderImpl();
		final BeanResolver beanResolver = Mockito.mock(BeanResolver.class);
		Assert.assertEquals(modelRepositoryBuilder, modelRepositoryBuilder.withBeanResolver(beanResolver));
		Assert.assertEquals(beanResolver, ReflectionTestUtils.getField(modelRepositoryBuilder, "beanResolver"));
	}
	
	@Test
	public final void withDomain() {
		final ModelRepositoryBuilder modelRepositoryBuilder = new ModelRepositoryBuilderImpl();
		final Artist artist = Mockito.mock(Artist.class);
		Assert.assertEquals(modelRepositoryBuilder, modelRepositoryBuilder.withDomain(artist));
		Assert.assertEquals(1, ((Collection<?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "domains")).size());
		Assert.assertEquals(artist, ((Collection<?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "domains")).iterator().next());
		
	}
	
	@Test
	public final void withMap() {
		final ModelRepositoryBuilder modelRepositoryBuilder = new ModelRepositoryBuilderImpl();
		final Artist artist = Mockito.mock(Artist.class);
		final Map<String,Object> map = new HashMap<>();
		map.put("artist", artist);
		Assert.assertEquals(modelRepositoryBuilder, modelRepositoryBuilder.withMap(map));
		Assert.assertEquals(1, ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).size());
		Assert.assertEquals(artist, ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).values().iterator().next());
		Assert.assertEquals("artist", ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).keySet().iterator().next());
		
	}
	
	@Test
	public final void withMapEntry() {
		final ModelRepositoryBuilder modelRepositoryBuilder = new ModelRepositoryBuilderImpl();
		final Artist artist = Mockito.mock(Artist.class);
		
		
		Assert.assertEquals(modelRepositoryBuilder, modelRepositoryBuilder.withMapEntry("artist", artist));
		Assert.assertEquals(1, ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).size());
		Assert.assertEquals(artist, ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).values().iterator().next());
		Assert.assertEquals("artist", ((Map<?,?>) ReflectionTestUtils.getField(modelRepositoryBuilder, "map")).keySet().iterator().next());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void buildMissingBeanResolver() {
		new ModelRepositoryBuilderImpl().build();
		
	}
	
    @Test
	public final void build() {
		final Artist artist = Mockito.mock(Artist.class);
		final ModelRepository modelRepository = new ModelRepositoryBuilderImpl().withBeanResolver(Mockito.mock(BeanResolver.class)).withDomain(artist).build();
		Assert.assertEquals(artist, modelRepository.get(artist.getClass()));
	}
    
    
    

}
