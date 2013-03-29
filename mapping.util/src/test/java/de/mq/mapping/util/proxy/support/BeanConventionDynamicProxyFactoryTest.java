package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

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
import de.mq.mapping.util.proxy.model.ArtistWeb;
import de.mq.mapping.util.proxy.model.Video;
import de.mq.mapping.util.proxy.model.VideoImpl;
import de.mq.mapping.util.proxy.model.VideoWeb;
import de.mq.mapping.util.proxy.model.VideoWebComparator;

public class BeanConventionDynamicProxyFactoryTest {
	
	
	
	private static final Integer HOT_SCORE = 10;
	private static final String NAME = "Kylie";


	private BeanResolver beanResolver;
	
	private final Comparator<VideoWeb> videoAOComparator = new VideoWebComparator(); 
	private final Converter<Object,Object> noConverter = new NoConverter();
	private final Action actionMock = new ActionMock();
	
	
	@Before
	public final void setup(){
		 beanResolver = Mockito.mock(BeanResolver.class);
		 
		 Mockito.when(beanResolver.getBeanOfType(VideoWebComparator.class)).thenReturn((VideoWebComparator) videoAOComparator);
		 Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn( (NoConverter) noConverter);
		 Mockito.when(beanResolver.getBeanOfType(ActionMock.class)).thenReturn( (ActionMock) actionMock);
	}
	
	
	
	@Test
	public final void testFactory()  {
		final AOProxyFactory factory = newProxy();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, newArtist());
		final ArtistWeb proxy = factory.createProxy(ArtistWeb.class, modelRepository );
		
		
		Assert.assertTrue(proxy.getClass().getSimpleName().startsWith("$Proxy"));
		
	}

	private  AOProxyFactory  newProxy() {
		final AOProxyFactory factory =  new BeanConventionDynamicProxyFactory();
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(factory);
		return factory;
	}

	private ArtistImpl newArtist() {
		return new ArtistImpl(NAME, HOT_SCORE);
	}
	
	@Test
	public final void testGetter() {
		final AOProxyFactory factory = newProxy();
		final Artist artist = newArtist();
		ReflectionTestUtils.setField(artist, "id", 4711L);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistWeb proxy = factory.createProxy(ArtistWeb.class, modelRepository);
		
		Assert.assertEquals(artist.name(), proxy.getName());
		Assert.assertEquals(artist.hotScore(), proxy.gethotScore());
		
		Assert.assertEquals(artist.id(),proxy.getId());
		
		
	}
	
	@Test
	public final void testCollection() {
		final AOProxyFactory factory = newProxy();
		final Video[] videos = new Video[] {new VideoImpl("Spinning around" , 2), new VideoImpl("Can't get you out of my head" , 1)   };
		
		final Artist artist = newArtist();
		artist.assign(videos);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistWeb proxy = factory.createProxy(ArtistWeb.class, modelRepository);
	
		int i=0;
		for(final VideoWeb ao : proxy.getVideos()){


			if( ao.getTitle().equals(videos[0].title())){
				Assert.assertEquals(ao.getId(), videos[0].id());
				i++;
				continue;
			}
			if( ao.getTitle().equals(videos[1].title())){
				Assert.assertEquals(ao.getId(), videos[1].id());
				i++;
				continue;
			}
			Assert.fail("Wrong title: " + ao.getTitle());
			
		}
		
		Assert.assertEquals(2, i);
	}
	
	
	
	@Test
	public final void testComparator(){
		final AOProxyFactory factory = newProxy();
		
		
		final Artist artist = newArtist();
		artist.assign(new VideoImpl("Where the wild roses grow",5));
		artist.assign(new VideoImpl("I should be so lucky",3));;
		artist.assign(new VideoImpl("Spinning around",4));
		artist.assign(new VideoImpl("Can't get you out of my head",2));
		artist.assign(new VideoImpl("Body Language",1));
		
		
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistWeb proxy = factory.createProxy(ArtistWeb.class, modelRepository);
		int i=1;
		for(final VideoWeb videoAO : proxy.getVideos()){
		   Assert.assertEquals(i, videoAO.getId().intValue()); 
			i++;
		}
		
	}
	
	
	
	@Test
	public final void testGetterProxy() {
		final AOProxyFactory factory = newProxy();
		final Artist artist = newArtist();
		
		Artist duetPartner = new ArtistImpl("Nick Cave", 99); 
		artist.assign(duetPartner);
		
	   
	   final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
	   
	   final ArtistWeb proxy = factory.createProxy(ArtistWeb.class, modelRepository);
	   
	   Assert.assertEquals(duetPartner.name(), proxy.getDuetPartner().getName()) ;
	   Assert.assertEquals(duetPartner.id(), proxy.getDuetPartner().getId()) ;
	  
	   
	}
	
	
	
	@Test
	public final void testSetters(){
		final AOProxyFactory factory = newProxy();
		final Artist artist = new ArtistImpl(null);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistWeb artistAO = factory.createProxy(ArtistWeb.class, modelRepository);
		
		artistAO.setName(NAME);
		
		artistAO.setHotScore(HOT_SCORE);
		
		
		Assert.assertEquals(NAME, artistAO.getName());
		Assert.assertEquals(HOT_SCORE, artistAO.gethotScore());
		
		
		
		
	}
	
	
	
	@Test
	public final void testGetterDomain(){
		final AOProxyFactory factory = newProxy();
		final Artist artist = newArtist();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		final ArtistWeb modelAO = factory.createProxy(ArtistWeb.class, modelRepository);
		Assert.assertEquals(artist, modelAO.getArtist());
	}
	
	
	@Test
	public final void testSetterDomain() {
		final AOProxyFactory factory = newProxy();
		
		final Artist oldModel = newArtist();
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, oldModel);
		final ArtistWeb modelAO = factory.createProxy(ArtistWeb.class, modelRepository);
		
		final Artist newModel = new ArtistImpl("Lady Gaga", 4711);
		Assert.assertEquals(oldModel , modelAO.getArtist());
		modelAO.setArtist(newModel);
		Assert.assertEquals(newModel, modelAO.getArtist());
	}
	
	@Test
	public final void testNoDomain() {
		final AOProxyFactory factory = newProxy();
		Assert.assertTrue( factory.createProxy(ArtistWeb.class, new ModelRepositoryImpl(beanResolver)).getClass().getSimpleName().startsWith("$Proxy"));
	
	}
	
	@Test
	public final void testEcecuteExistingMethod() {
		final AOProxyFactory factory = newProxy();
		final GregorianCalendar cal = new GregorianCalendar(1968,4,28);
		final ArtistWeb artistWeb = factory.createProxy(ArtistWeb.class, new ModelRepositoryImpl(beanResolver, cal));
		Assert.assertTrue(artistWeb.toString().startsWith(BeanConventionDynamicProxyFactory.class.getName()));
	}
	
	
	@Test(expected=ClassNotFoundException.class)
	public final void testExceptionTranslationList() throws Exception {
		final BeanConventionDynamicProxyFactory factory = new BeanConventionDynamicProxyFactory();
		final ArtistController controller = factory.createProxy(ArtistController.class, new ModelRepositoryImpl(beanResolver, new ArtistControllerImpl())); 
		controller.artist(-1L);
	}
	
	@Test()
	public final void testExceptionTranslationListNoException() throws Exception {
		
		final BeanConventionCGLIBProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		final ArtistController controller = factory.createProxy(ArtistController.class, new ModelRepositoryImpl(beanResolver, new ArtistControllerImpl())); 
		ArtistAO result = controller.artist(19680528L);
		Assert.assertEquals("Kylie", result.getName());
		Assert.assertEquals(10, result.gethotScore().intValue());
	}
	
	
	@Test
	public final void testPermGem() {
		final AOProxyFactory factory = newProxy();
		final Class<? extends ArtistWeb> lastClass = factory.createProxy(ArtistWeb.class, new ModelRepositoryImpl(null,  newArtist())).getClass();
		long t1 = new Date().getTime();
		for (int i = 0; i < 1e6; i++) {
			final Class<? extends ArtistWeb> clazz = factory.createProxy(ArtistWeb.class, new ModelRepositoryImpl(null, newArtist())).getClass();
			Assert.assertEquals(Proxy.class, clazz.getSuperclass());
			
			Assert.assertEquals(lastClass, clazz);
		}

		Assert.assertTrue((new Date().getTime() - t1) < 5e3);
	   
	}
	

}
