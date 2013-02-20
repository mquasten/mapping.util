package de.mq.mapping.util.proxy.model.support;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mq.mapping.util.proxy.model.ArtistAO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/artists.xml"})
public class ArtistFactoryIntegrationTest {
	
	
	/*@Autowired
	private ArtistAO artist; */
	
	@Autowired
	private ApplicationContext ctx;
	
	private final Map<Object,Integer> counters = new HashMap<>();
	
	@Test
	public final void createArtist() {
		
		
		for(int i=0; i < 100; i++) {
			final ArtistAO artist = ctx.getBean(ArtistAO.class);
			if ( ! counters.containsKey(artist)){
				counters.put(artist, 0);
			}
			counters.put(artist, 1+ counters.get(artist));
		} 
		
		Assert.assertEquals(10, counters.size());
		for(Integer count : counters.values()){
			Assert.assertEquals(10, (int)  count);
		}
	}

}

