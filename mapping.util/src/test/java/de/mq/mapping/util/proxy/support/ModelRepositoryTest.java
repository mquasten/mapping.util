package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.NoModel;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.model.ProfileAO;
import de.mq.mapping.util.proxy.model.String2IntegerConverter;

public class ModelRepositoryTest {
	
	
	private  BeanResolver beanResolver;
	
	final Converter<Object,Object>  noConverter = new  NoConverter();
	final Converter<String, Integer> string2IntegerConverter = new String2IntegerConverter();
	final Converter<Number,String> number2StringConverter = new Number2StringConverter();
	
	@Before
	public final void setUp() {
		//System.setSecurityManager(null);
		beanResolver = Mockito.mock(BeanResolver.class);
		Mockito.when(beanResolver.getBeanOfType(NoConverter.class)).thenReturn((NoConverter) noConverter);
		Mockito.when(beanResolver.getBeanOfType(String2IntegerConverter.class)).thenReturn((String2IntegerConverter) string2IntegerConverter);
		Mockito.when(beanResolver.getBeanOfType(Number2StringConverter.class)).thenReturn( (Number2StringConverter) number2StringConverter);
	}


	@SuppressWarnings("unchecked")
	@Test
	public final void testConstructorDomainClasses() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		
		final Model domain =  Mockito.mock(Model.class);
		
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, domain);
		
		final Map<Key, Object> items =  (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		
		Assert.assertEquals(1, items.size());
	
		Assert.assertEquals(domain, items.values().iterator().next());
		Assert.assertEquals(new KeyImpl(domain.getClass()), items.keySet().iterator().next());
		
		
	}
	/*
	
	@Test
	public final void testConstructorWeb() {
		final ModelRepository modelRepository = new ModelRepositoryImpl(ArtistAO.class);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, items.size());
		
		Assert.assertEquals(ArtistImpl.class, items.values().iterator().next().getClass());
		Assert.assertEquals(new KeyImpl(ArtistImpl.class), items.keySet().iterator().next());
		
	}
	*/
	
	
	@Test
	public final void testConstructorMap() {
		final String fieldName = "kyliesBirthDate";
		final Map<String,Object> map = new HashMap<String,Object>();
		final Date date = new GregorianCalendar(1968, 4, 28).getTime();
		map.put(fieldName, date);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, items.size());
		Assert.assertEquals(new KeyImpl(fieldName), items.keySet().iterator().next());
		Assert.assertEquals(date, items.values().iterator().next());
		
		
	}
	
	
	@Test
	public final void testPutDoman() {
		final Model model = Mockito.mock(Model.class);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		
		modelRepository.put(model);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, items.size());
		Assert.assertEquals(new KeyImpl(model.getClass()), items.keySet().iterator().next());
		Assert.assertEquals(model, items.values().iterator().next());
	}
	
	@Test
	public final void testPutMap(){
		final Map<String,Object> model = new HashMap<String,Object>();
		model.put("artist", "Kylie");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		
		modelRepository.put(model);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(1, items.size());
		Assert.assertEquals(new KeyImpl("artist"), items.keySet().iterator().next());
		Assert.assertEquals("Kylie", items.values().iterator().next());
		
	}
	
	
	@Test
	public final void testPutDomanField() {
		final String fieldName = "Kylie";
		final Artist model = new ArtistImpl(fieldName);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, model);
		
		
		modelRepository.put(ArtistImpl.class,"name" ,fieldName, NoConverter.class);
		
		Assert.assertEquals(fieldName, model.name());
		
		
	}
	
	@Test
	public final void testPutDomainFieldWrongType() {
		
		final Artist artist = new ArtistImpl("Kylie"); 
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		
		final String errorValue = "dontLetMeGetMe";
		modelRepository.put(ArtistImpl.class , "hotScore" , errorValue, NoConverter.class);
		
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.containsKey(new KeyImpl(ArtistImpl.class, "hotScore")));
		Assert.assertEquals(errorValue, items.get(new KeyImpl(ArtistImpl.class, "hotScore")));
		
	}
	
	@Test
	public final void testPutMapValue() {
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		final String fieldName = "artist";
		final String fieldValue = "kylie";
		modelRepository.put(NoModel.class, fieldName, fieldValue, NoConverter.class);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		
		
		Assert.assertEquals(1, items.size());
		Assert.assertEquals(new KeyImpl(fieldName), items.keySet().iterator().next());
		Assert.assertEquals(fieldValue, items.values().iterator().next());
		
	}
	
	@Test
	public final void testGetDomain(){
		final Model model = Mockito.mock(Model.class);
		
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, model);
		Assert.assertEquals(model, modelRepository.get(model.getClass()));
	}
	
	@Test
	public final void testGetMap() {
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("artist", "Kylie");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, model);
		Assert.assertEquals(model, modelRepository.get(NoModel.class));
	}
	
	
	@Test
	public final void testGetDomainField(){
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		
		Assert.assertEquals(artist.name(), modelRepository.get(artist.getClass(), "name"));
		Assert.assertEquals(artist.hotScore(), modelRepository.get(artist.getClass(), "hotScore"));
		
		
	}
	
	
	@Test
	public final void testGetMapValue(){
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put("artist", "Kylie");
		map.put("hotScore", "1");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, map);
		Assert.assertTrue(map.size() > 0 );
		for(final String key : map.keySet()){
			Assert.assertEquals(map.get(key), modelRepository.get(NoModel.class, key));
		}
		
	}
	
	
	@Test
	public final void testGetDomainFieldError(){
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		final String errorValue = "don'tLetMeGetMe";
		modelRepository.put(ArtistImpl.class, "hotScore", errorValue, NoConverter.class);
		Assert.assertTrue(items.containsKey(new KeyImpl(ArtistImpl.class , "hotScore")));
		Assert.assertEquals((int) 10,(int) artist.hotScore());
		Assert.assertEquals(errorValue, modelRepository.get(ArtistImpl.class, "hotScore"));
		
		
		
		
	}
	
	@Test
	public final void testGetDomainFieldErrorResetSetter(){
		int validValue = 10;
		final String errorValue = "don'tLetMeGetMe";
		final Artist artist = new ArtistImpl("Kylie");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		
		items.put(new KeyImpl(ArtistImpl.class, "hotScore"), errorValue);
		
		
		Assert.assertEquals(errorValue, modelRepository.get(ArtistImpl.class, "hotScore"));
		
		modelRepository.put(ArtistImpl.class, "hotScore",validValue, NoConverter.class);
		
		Assert.assertEquals(validValue,(int) artist.hotScore());
		
		
		Assert.assertFalse(items.containsKey(new KeyImpl(ArtistImpl.class , "hotScore")));
		
	}
	
	@Test
	public final void testGetDomainFieldErrorResetDomain(){
		
		final String errorValue = "don'tLetMeGetMe";
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		
		items.put(new KeyImpl(ArtistImpl.class, "hotScore"), errorValue);
		
		
		Assert.assertEquals(errorValue, modelRepository.get(ArtistImpl.class, "hotScore"));
		
		modelRepository.put(new ArtistImpl("Beyonce"));
		
		
		
		
		Assert.assertFalse(items.containsKey(new KeyImpl(ArtistImpl.class , "hotScore")));
	
	}
	
	@Test
	public final void testGetChilds() {
		final String errorValue = "don'tLetMeGetMe";
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepositoryImpl modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		Assert.assertTrue(modelRepository.childKeys(artist.getClass()).isEmpty());
      modelRepository.put(ArtistImpl.class, "hotScore", errorValue, NoConverter.class);
      Assert.assertFalse(modelRepository.childKeys(artist.getClass()).isEmpty());
      Assert.assertEquals(new KeyImpl(ArtistImpl.class, "hotScore"), modelRepository.childKeys(artist.getClass()).iterator().next());
		
	}
	@Test
	public final void testDeleteChilds() {
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepositoryImpl modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		
		Key errorKey = new KeyImpl(ArtistImpl.class, "hotScore");
		items.put(errorKey, "don'tLetMeGetMe");
		Assert.assertEquals(2, items.size());
		Assert.assertTrue(items.containsKey(errorKey));
		modelRepository.deleteChilds(ArtistImpl.class);
		Assert.assertFalse(items.containsKey(errorKey));
		Assert.assertTrue(items.containsKey(new KeyImpl(ArtistImpl.class)));
		
	}
	
	@Test
	public final void testHasErrors() {
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		@SuppressWarnings("unchecked")
		final Map<Key, Object> items =   (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		items.put(new KeyImpl(ArtistImpl.class, "hotScore"), "don'tLetMeGetMe" );
		
		
		Assert.assertTrue(modelRepository.hasError(ArtistImpl.class, "hotScore"));
		
	}
	
	@Test
	public final void testPutConverter() {
		final Artist artist = new ArtistImpl("Kylie");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist, 1);
		
		modelRepository.put(ArtistImpl.class, "hotScore", "10", String2IntegerConverter.class);
		
		Mockito.verify(beanResolver).getBeanOfType(String2IntegerConverter.class);
		Assert.assertEquals(10,(int) artist.hotScore());
	
	}
	
	@Test
	public final void testGetConverter() {
		final Artist artist = new ArtistImpl("Kylie", 10);
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, artist);
		
		Assert.assertEquals(String.valueOf(artist.hotScore()), modelRepository.get(ArtistImpl.class,"hotScore" ,Number2StringConverter.class, String.class ));
		Mockito.verify(beanResolver).getBeanOfType(Number2StringConverter.class);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void testDomainObjectNotFound() {
		new ModelRepositoryImpl(beanResolver).get(ArtistImpl.class);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public final void testConverterCreationException() {
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		Mockito.when(beanResolver.getBeanOfType(Converter.class)).thenThrow(new IllegalStateException());
		modelRepository.put(new ArtistImpl("Kylie"));
		modelRepository.put(ArtistImpl.class, "hotScore", 10L,  Converter.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public final void testGetFieldAccessError() {
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		
		modelRepository.put(new ArtistImpl("Kylie"));
		
		modelRepository.get(ArtistImpl.class, "dontLetMeGetMe");
	}
	
/*	
	@Test(expected=IllegalStateException.class)
	public final void testErrorCreateDomainObject() {
		new ModelRepositoryImpl(WebMock.class);
	} */
	
	@Test
	public final void testNoModel() {
		Assert.assertEquals(HashMap.class, new ModelRepositoryImpl(beanResolver, ProfileAO.class).get(NoModel.class).getClass());
	}
	
	@Test
	public final void testDomainAndMapModel() {
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver);
		modelRepository.put(new ArtistImpl("Kylie"));
		Assert.assertEquals(HashMap.class, modelRepository.get(NoModel.class).getClass());
	}
	
}


abstract interface WebMock {
	
	@GetterDomain(clazz=Artist.class) 
	Artist getArtist();
}

