package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.model.ArtistSelector;
import de.mq.mapping.util.proxy.model.HotArtistAO;
import de.mq.mapping.util.proxy.model.HotArtistImpl;
import de.mq.mapping.util.proxy.model.Video;
import de.mq.mapping.util.proxy.model.VideoAO;
import de.mq.mapping.util.proxy.model.VideoAOComparator;
import de.mq.mapping.util.proxy.model.VideoImpl;

public class BasicGetterCollectionInterceptorTest {

	private BeanResolver beanResolver ;
	private ModelRepository modelRepository;
	private AOProxyFactory proxyFactory;
	
	private final Converter<Object,Object> noConverter = new NoConverter();
	
	
	@Before
	public final void setup() {
		 beanResolver = Mockito.mock(BeanResolver.class);
		 modelRepository = Mockito.mock(ModelRepository.class);
		 proxyFactory = Mockito.mock(AOProxyFactory.class);
	     Mockito.when(modelRepository.beanResolver()).thenReturn(beanResolver);
	     Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(proxyFactory);
	     Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn((NoConverter) noConverter);
	     Mockito.when(beanResolver.getBeanOfType(EntryConverter.class)).thenReturn((new EntryConverter()) );
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public final void invoke() throws Throwable {
		
		final List<Video> videos = new ArrayList<Video>();
		videos.add(Mockito.mock(Video.class));
	
		Mockito.when(modelRepository.get(ArtistImpl.class, "videos")).thenReturn(videos);
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getVideos");
		
		
		VideoAO videoAO = Mockito.mock(VideoAO.class);
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class), Mockito.any(ModelRepository.class))).thenReturn(videoAO);

		final List<VideoAO> results = (List<VideoAO>) interceptor.invoke(method, new Object[] {});
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(videoAO, results.iterator().next());
		Mockito.verify(modelRepository).get(ArtistImpl.class, "videos");
		Assert.assertEquals(ArrayList.class, results.getClass());

	}

	@Test
	public final void invokeNullFromRepository() throws Throwable {
		Mockito.when(modelRepository.get(ArtistImpl.class, "videos")).thenReturn(null);
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(ArtistAO.class, "getVideos");
		Assert.assertTrue(((Collection<?>) interceptor.invoke(method, new Object[] {})).isEmpty());
		Mockito.verifyNoMoreInteractions(proxyFactory);
	}

	@Test(expected = IllegalStateException.class)
	public final void invokeMissingAnnotation() throws Throwable {
		Mockito.when(modelRepository.get(ArtistImpl.class, "time")).thenReturn(new ArrayList<Video>());
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(GregorianCalendar.class, "getTime");
		interceptor.invoke(method, new Object[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public final void invokeComparatorWithSet() throws Throwable {
		final List<Video> videos = new ArrayList<Video>();
		videos.add(Mockito.mock(Video.class));
		Mockito.when(modelRepository.get(ArtistImpl.class, "videos")).thenReturn(videos);
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(AOMock.class, "getVideos");
		interceptor.invoke(method, new Object[] {});
	}

	@SuppressWarnings({ "unchecked" })
	@Test()
	public final void invokeNoComparator() throws Throwable {

		final List<Video> videos = new ArrayList<Video>();
		videos.add(Mockito.mock(Video.class));

		Mockito.when(modelRepository.get(ArtistImpl.class, "videos2")).thenReturn(videos);
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(AOMock.class, "getVideos2");
		VideoAO videoAO = Mockito.mock(VideoAO.class);
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class), Mockito.any(ModelRepository.class))).thenReturn(videoAO);
		Set<VideoAO> results = (Set<VideoAO>) interceptor.invoke(method, new Object[] {});
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(videoAO, results.iterator().next());
		Assert.assertEquals(HashSet.class, results.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Test()
	public final void invokeConverterBeanNotFound() throws Throwable {
		VideoAO videoAO = Mockito.mock(VideoAO.class);
		final List<Video> videos = new ArrayList<Video>();
		videos.add(Mockito.mock(Video.class));

		Mockito.when(modelRepository.get(ArtistImpl.class, "videos3")).thenReturn(videos);
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(AOMock.class, "getVideos3");

		Mockito.when(beanResolver.getBeanOfType(Number2StringConverter.class)).thenThrow(new NoSuchBeanDefinitionException(Number2StringConverter.class));
		Mockito.when(proxyFactory.createProxy(Mockito.any(Class.class), Mockito.any(ModelRepository.class))).thenReturn(videoAO);
		Set<VideoAO> results = (Set<VideoAO>) interceptor.invoke(method, new Object[] {});
		
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(videoAO, results.iterator().next());
		Assert.assertEquals(HashSet.class, results.getClass());
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void invokeMap() throws Throwable {
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(AOMock.class, "getVideos4");
		final Map<String,Video> videos = new HashMap<>();
		final Video video = Mockito.mock(Video.class);
		Mockito.when(video.title()).thenReturn("where the wild roses grow");
		videos.put(video.title(), video);
		Mockito.when(modelRepository.get(ArtistImpl.class, "videos4")).thenReturn(videos);
		
		VideoAO videoAO = Mockito.mock(VideoAO.class);
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modelRepositoryCaptor = ArgumentCaptor.forClass(ModelRepository.class);
		Mockito.when(proxyFactory.createProxy(classCaptor.capture(), modelRepositoryCaptor.capture())).thenReturn(videoAO);
		
	
		final Collection<VideoAO> results = (Collection<VideoAO>) interceptor.invoke(method, new Object[] {});
		
	
		Assert.assertEquals(videoAO, results.iterator().next());
		
		Assert.assertEquals(VideoAO.class, classCaptor.getValue());
		ModelRepository modelRepository =  modelRepositoryCaptor.getValue();
		Assert.assertEquals(ModelRepositoryImpl.class, modelRepository.getClass());
		final Map<?,Object> items = (Map<?, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, items.size());
		Assert.assertEquals(video, items.values().iterator().next());
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void polymorhismCollection() throws Throwable {
		
		final ArtistSelector artistSelector = new ArtistSelector();
		Mockito.when(beanResolver.getBeanOfType(ArtistSelector.class)).thenReturn(artistSelector);
		
		final ArgumentCaptor<Class> classCapture = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<ModelRepository> modellCapture = ArgumentCaptor.forClass(ModelRepository.class);
		final Video video  = new VideoImpl("Don'tCha");
		final Artist artist1 = new HotArtistImpl("Nicole Scherzinger", 500e3);
		final Artist artist2 = new ArtistImpl("Jessica Sutta");
		video.assign(artist1);
		video.assign(artist2);
		
		HotArtistAO hotArtistAO = Mockito.mock(HotArtistAO.class);
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
	    Mockito.when(proxyFactory.createProxy(classCapture.capture(), modellCapture.capture())).thenReturn(hotArtistAO, artistAO);
		Mockito.when(modelRepository.get(VideoImpl.class, "artists")).thenReturn(video.artists());
		final Interceptor interceptor = new BasicGetterCollectionInterceptorImpl(modelRepository);
		final Method method = ReflectionUtils.findMethod(VideoAO.class, "getArtists");
		
		final List<?> results = (List<?>) interceptor.invoke(method, new Object[] {});
		Assert.assertEquals(2, results.size());
		Assert.assertEquals(hotArtistAO, results.get(0));
		Assert.assertEquals(artistAO, results.get(1));
		
		
		
		Assert.assertTrue(classCapture.getAllValues().contains(HotArtistAO.class));
		Assert.assertTrue(classCapture.getAllValues().contains(ArtistAO.class));
		Assert.assertEquals(artist1, modellCapture.getAllValues().get(0).get(HotArtistImpl.class));
		Assert.assertEquals(artist2, modellCapture.getAllValues().get(1).get(ArtistImpl.class));
		
	}
	
	

}

interface AOMock {
	@GetterProxyCollection(clazz = ArtistImpl.class, name = "videos", proxyClass = VideoAO.class, collectionClass = HashSet.class, comparator = VideoAOComparator.class)
	public abstract List<VideoAO> getVideos();

	@GetterProxyCollection(clazz = ArtistImpl.class, name = "videos2", proxyClass = VideoAO.class, collectionClass = HashSet.class)
	public abstract Set<VideoAO> getVideos2();
	
	@GetterProxyCollection(clazz = ArtistImpl.class, name = "videos3", proxyClass = Number2StringConverter.class, collectionClass = HashSet.class)
	public abstract Set<VideoAO> getVideos3();
	
	@GetterProxyCollection(collectionClass=ArrayList.class, name="videos4",proxyClass=VideoAO.class, clazz = ArtistImpl.class, converter=EntryConverter.class)
	public abstract Collection<VideoAO> getVideos4();
}


class EntryConverter implements Converter<Map.Entry<?, Video>, Video> {

	@Override
	public Video convert(Entry<?, Video> source) {
		return source.getValue();
	}

	
	
}

