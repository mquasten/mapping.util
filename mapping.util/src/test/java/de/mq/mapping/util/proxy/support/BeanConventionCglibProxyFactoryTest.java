package de.mq.mapping.util.proxy.support;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import junit.framework.Assert;

import net.sf.cglib.proxy.Factory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.ActionMock;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistController;
import de.mq.mapping.util.proxy.model.ArtistControllerImpl;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.model.Video;
import de.mq.mapping.util.proxy.model.VideoAO;
import de.mq.mapping.util.proxy.model.VideoAOComparator;
import de.mq.mapping.util.proxy.model.VideoImpl;

public class BeanConventionCglibProxyFactoryTest {

	private static final Integer HOT_SCORE = 10;
	private static final String NAME = "Kylie";

	
	private  BeanResolver beanResolver;
	
	private final Comparator<VideoAO> videoAOComparator = new VideoAOComparator(); 
	private final Converter<Object,Object> noConverter = new NoConverter();
	private final Action actionMock = new ActionMock();
	
	
	@Before
	public final void setup(){
		 beanResolver = Mockito.mock(BeanResolver.class);
		 
		 Mockito.when(beanResolver.getBeanOfType(VideoAOComparator.class)).thenReturn((VideoAOComparator) videoAOComparator);
		 Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn( (NoConverter) noConverter);
		 Mockito.when(beanResolver.getBeanOfType(ActionMock.class)).thenReturn( (ActionMock) actionMock);
	}
	
	
	@Test
	public final void testFactory() {
		
		
		final AOProxyFactory factory = newProxyFactory();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, newArtist());
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);

		Assert.assertTrue(proxy.getClass().getSimpleName().startsWith("ArtistAO$$EnhancerByCGLIB"));

	}

	private AOProxyFactory newProxyFactory() {
		final AOProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(factory);
		
		return factory;
	}

	private ArtistImpl newArtist() {
		return new ArtistImpl(NAME, HOT_SCORE);
	}

	@Test
	public final void testGetter() {
		final AOProxyFactory factory = newProxyFactory();
		final Artist artist = newArtist();
		ReflectionTestUtils.setField(artist, "id", 4711L);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);

		Assert.assertEquals(artist.name(), proxy.getName());
		Assert.assertEquals(artist.hotScore(), proxy.gethotScore());

		Assert.assertEquals(artist.id(), proxy.getId());

	}

	@Test
	public final void testCollection() {
		
		final AOProxyFactory factory = newProxyFactory();
		
		
		
		final Video[] videos = new Video[] { new VideoImpl("Spinning around", 2), new VideoImpl("Can't get you out of my head", 1) };

		final Artist artist = newArtist();
		artist.assign(videos);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);

		int i = 0;
		for (final VideoAO ao : proxy.getVideos()) {

			if (ao.getTitle().equals(videos[0].title())) {
				Assert.assertEquals(ao.getId(), videos[0].id());
				i++;
				continue;
			}
			if (ao.getTitle().equals(videos[1].title())) {
				Assert.assertEquals(ao.getId(), videos[1].id());
				i++;
				continue;
			}
			Assert.fail("Wrong title: " + ao.getTitle());

		}

		Assert.assertEquals(2, i);
	}

	@Test
	public final void testComparator() {
		final AOProxyFactory factory = newProxyFactory();

		final Artist artist = newArtist();
		artist.assign(new VideoImpl("Where the wild roses grow", 5));
		artist.assign(new VideoImpl("I should be so lucky", 3));
		;
		artist.assign(new VideoImpl("Spinning around", 4));
		artist.assign(new VideoImpl("Can't get you out of my head", 2));
		artist.assign(new VideoImpl("Body Language", 1));

		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);
		int i = 1;
		for (final VideoAO videoAO : proxy.getVideos()) {
			Assert.assertEquals(i, videoAO.getId().intValue());
			i++;
		}

	}

	@Test
	public final void testGetterProxy() {
	
		final AOProxyFactory factory = newProxyFactory();
		final Artist artist = newArtist();

		Artist duetPartner = new ArtistImpl("Nick Cave", 99);
		artist.assign(duetPartner);

		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);

		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);

		Assert.assertEquals(duetPartner.name(), proxy.getDuetPartner().getName());
		Assert.assertEquals(duetPartner.id(), proxy.getDuetPartner().getId());

	}

	@Test
	public final void testSetters() {
		final AOProxyFactory factory = newProxyFactory();
		final Artist artist = new ArtistImpl(null);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistAO artistAO = factory.createProxy(ArtistAO.class, modelRepository);

		artistAO.setName(NAME);

		artistAO.setHotScore(HOT_SCORE);

		Assert.assertEquals(NAME, artistAO.getName());
		Assert.assertEquals(HOT_SCORE, artistAO.gethotScore());

	}

	@Test
	public final void testGetterDomain() {
		final AOProxyFactory factory = newProxyFactory();
		final Artist artist = newArtist();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistAO modelAO = factory.createProxy(ArtistAO.class, modelRepository);
		Assert.assertEquals(artist, modelAO.getArtist());
	}

	@Test
	public final void testSetterDomain() {
		final AOProxyFactory factory = newProxyFactory();

		final Artist oldModel = newArtist();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, oldModel);
		final ArtistAO modelAO = factory.createProxy(ArtistAO.class, modelRepository);

		final Artist newModel = new ArtistImpl("Lady Gaga", 4711);
		Assert.assertEquals(oldModel, modelAO.getArtist());
		modelAO.setArtist(newModel);
		Assert.assertEquals(newModel, modelAO.getArtist());
	}

	@Test
	public final void testWithoutDomain() {
		final AOProxyFactory factory = newProxyFactory();
		Assert.assertTrue(factory.createProxy(ArtistAO.class, new ModelRepositoryImpl(beanResolver)).getClass().getSimpleName().startsWith("ArtistAO$$EnhancerByCGLIB"));
	}
	
	@Test(expected=ClassNotFoundException.class)
	public final void testExceptionTranslationList() throws Exception {
		final BeanConventionCGLIBProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		final ArtistController controller = factory.createProxy(ArtistControllerImpl.class,  new ModelRepositoryImpl(beanResolver, new ArtistControllerImpl())); 
		controller.artist(-1L);
	}
	
	@Test()
	public final void testExceptionTranslationListNoException() throws Exception {
		
		final BeanConventionCGLIBProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		final ArtistController controller = factory.createProxy(ArtistControllerImpl.class,  new ModelRepositoryImpl(beanResolver, new ArtistControllerImpl())); 
		final ArtistAO result = controller.artist(19680528L);
		Assert.assertEquals("Kylie", result.getName());
		Assert.assertEquals(10, result.gethotScore().intValue());
	}
	
	
	
	

	@Test
	public final void testPermGem() {
	
		final AOProxyFactory factory = newProxyFactory();
		final Class<? extends ArtistAO> lastClass = factory.createProxy(ArtistAO.class, new ModelRepositoryImpl(null, newArtist())).getClass();
		long t1 = new Date().getTime();
		for (int i = 0; i < 10e6; i++) {
			final Class<? extends ArtistAO> clazz = factory.createProxy(ArtistAO.class, new ModelRepositoryImpl(null, newArtist())).getClass();
			Assert.assertEquals(ArtistAO.class, clazz.getSuperclass());

			Assert.assertEquals(lastClass, clazz);
		}

		Assert.assertTrue((new Date().getTime() - t1) < 5e3);
		
	}
	

	
	@Test
	public final void testProxyExistsInMap() {
		
		final ModelRepository modelRepository = Mockito.mock(ModelRepository.class);
		final BeanConventionCGLIBProxyFactory factory = new BeanConventionCGLIBProxyFactory();
	
		final Factory factoryMock = Mockito.mock(Factory.class); 
		@SuppressWarnings("unchecked")
		final Map<Class<?>, Factory> proxies = (Map<Class<?>, Factory>) ReflectionTestUtils.getField(factory, "proxies");
		proxies.put(ArtistAO.class, factoryMock);
		
		ReflectionTestUtils.invokeMethod(factory, "add2Mapp", ArtistAO.class, modelRepository);
		
		Assert.assertEquals(factoryMock, proxies.get(ArtistAO.class));
	}

}
