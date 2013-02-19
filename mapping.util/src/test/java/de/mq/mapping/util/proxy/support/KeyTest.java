package de.mq.mapping.util.proxy.support;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.support.KeyImpl.KeyType;

public class KeyTest {
	
	@Test
	public final void testCreateDomain(){
		final Key key = new KeyImpl(ArtistImpl.class);
		Assert.assertEquals(ArtistImpl.class.getName(), ReflectionTestUtils.getField(key, "name"));
		Assert.assertEquals(KeyImpl.KeyType.Domain, ReflectionTestUtils.getField(key, "keyType"));
	}
	
	@Test
	public final void testCreateMap(){
		final Key key = new KeyImpl("fieldName");
		Assert.assertEquals("fieldName", ReflectionTestUtils.getField(key, "name"));
		Assert.assertEquals(KeyImpl.KeyType.Map, ReflectionTestUtils.getField(key, "keyType"));
	}
	
	@Test
	public final void testCreateError(){
		final Key key = new KeyImpl(ArtistImpl.class, "name");
		Assert.assertEquals(ArtistImpl.class.getName() +".name" , ReflectionTestUtils.getField(key, "name"));
		Assert.assertEquals(KeyImpl.KeyType.Error, ReflectionTestUtils.getField(key, "keyType"));
	}
	
	@Test
	public final void testCreateChild(){
		final Key key = new KeyImpl(ArtistImpl.class, "name", KeyImpl.KeyType.Cache);
		Assert.assertEquals(ArtistImpl.class.getName() +".name" , ReflectionTestUtils.getField(key, "name"));
		Assert.assertEquals(KeyImpl.KeyType.Cache, ReflectionTestUtils.getField(key, "keyType"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void testCreateChildWrongType() {
		new KeyImpl(ArtistImpl.class, "name", KeyImpl.KeyType.Domain);
	}
	
	@Test
	public final void testEquals() {
		Assert.assertTrue(new KeyImpl("name").equals(new KeyImpl("name")));
		Assert.assertFalse(new KeyImpl("name").equals(new KeyImpl("dontLetMeGetMe")));
		Assert.assertFalse(new KeyImpl("name").equals(new String("name")));
		
		Assert.assertTrue(new KeyImpl(Object.class, "name").equals(new KeyImpl(Object.class, "name")));
		Assert.assertFalse(new KeyImpl(Object.class, "name").equals( new KeyImpl(Object.class, "don'tLetMeGetMe")));
		Assert.assertFalse(new KeyImpl(Object.class, "name").equals(new KeyImpl( "name")));
		Assert.assertFalse(new KeyImpl(Object.class, "name").equals(new KeyImpl(Date.class, "name")));
		Assert.assertFalse(new KeyImpl(Object.class, "name").equals(new KeyImpl(Date.class, "dontLetMeGetMe")));
	}
	
	@Test
	public final void testHashCode(){
		Assert.assertEquals(KeyImpl.KeyType.Map.hashCode() + "name".hashCode(), new KeyImpl("name").hashCode());
	}
	
	@Test
	public final void testToString(){
		Assert.assertEquals("type=Map, name=name", new KeyImpl("name").toString());
	}
	
	@Test
	public final void testHasParent() {
		final Key key = new KeyImpl(ArtistImpl.class, "name");
		Assert.assertTrue(key.hasParent(ArtistImpl.class));
		Assert.assertFalse(key.hasParent(Artist.class));
	}
	
	@Test
	public final void testHasParentNoChildType() {
		final Key key = new KeyImpl(ArtistImpl.class.getName()+".name");
		Assert.assertFalse(key.hasParent(ArtistImpl.class));
		
	}
	
	@Test()
	public final void testIsMapKey() {
		Assert.assertTrue(new KeyImpl("name").isMapKey());
		Assert.assertFalse(new KeyImpl(ArtistImpl.class).isMapKey());
		Assert.assertFalse(new KeyImpl(ArtistImpl.class, "name").isMapKey());
		Assert.assertFalse(new KeyImpl(ArtistImpl.class, "name", KeyType.Cache).isMapKey());
	}
	
	@Test
	public final void testName() {
		Assert.assertEquals("name", new KeyImpl("name").name());
	}
	
	@Test
	public final void keyTypes() {
		Assert.assertEquals(4, KeyImpl.KeyType.values().length);
		for(KeyType keyType : KeyType.values()){
			Assert.assertEquals(keyType, KeyType.valueOf(keyType.name()));
		}
	}

}
