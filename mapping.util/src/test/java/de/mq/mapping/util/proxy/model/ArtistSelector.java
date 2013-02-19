package de.mq.mapping.util.proxy.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

public class ArtistSelector  implements Converter<Artist, Class<?>>{

	
	private Map<Class<? extends Artist>, Class<?>> mappings = new HashMap<>();
	
	
	public ArtistSelector() {
		mappings.put(HotArtistImpl.class, HotArtistAO.class);
		mappings.put(ArtistImpl.class, ArtistAO.class);
	}
	
	@Override
	public Class<?> convert(final Artist source) {
		return mappings.get(source.getClass());
	}

}
