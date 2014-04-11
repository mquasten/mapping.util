package de.mq.mapping.util.proxy.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.NullObjectResolver;

@Component()
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class BasicNullObjectResolverImpl implements NullObjectResolver{

	private final  Map<Class<?>, Object> nullObjects = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public final <T> T forType(Class<? extends T> clazz) {
		objectExistsGuard(clazz);
		return (T) nullObjects.get(clazz);
	}
	
	protected void objectExistsGuard(Class<?> clazz) {
		
	}
	
	protected final <T> void  put(Class<? extends T> clazz, T nullValue) {
		nullObjects.put(clazz, nullValue);
	}

}
