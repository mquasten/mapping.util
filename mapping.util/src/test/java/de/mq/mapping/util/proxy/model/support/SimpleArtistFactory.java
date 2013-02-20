package de.mq.mapping.util.proxy.model.support;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;


@Configuration
public class SimpleArtistFactory {
	
	private final  AOProxyFactory aoProxyFactory;
	
	
	
	
	public SimpleArtistFactory(final AOProxyFactory aoProxyFactory) {
		this.aoProxyFactory=aoProxyFactory;
	}
	
	@Bean(name="artist")
	@Scope( proxyMode=ScopedProxyMode.NO , value="hotLadies")
	public  ArtistAO artistAO() {
		final Artist artist = new ArtistImpl("Kylie");
		
		return aoProxyFactory.createProxy(ArtistAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(Mockito.mock(BeanResolver.class)).withDomain(artist).build());
				
				
	}
	
	
	

}
