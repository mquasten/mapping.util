package de.mq.mapping.util.proxy.model.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


	
	public class SimpleArtistScope implements Scope {
		
		private Map<String, Object> beanCache = new HashMap<>();
		private Map<String, Integer> counters = new HashMap<>();
		
		private int max = 10;

		@Override
		final public Object get(final String name, final ObjectFactory<?> factory) {
			
			if(  ! counters.containsKey(name)) {
				counters.put(name, 0);
			} else if (counters.get(name) >= max ) {
				remove(name);
				counters.put(name, 0);
			}
			
			if( ! beanCache.containsKey(name)) {
				beanCache.put(name, factory.getObject());
			} 
			
			
			
			counters.put(name, counters.get(name)+1);
			return beanCache.get(name);
		}

		
		@Override
		final public void registerDestructionCallback(String arg0, Runnable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		final public Object remove(String name) {
			counters.remove(name);
			return beanCache.remove(name);
		}

		@Override
		final public Object resolveContextualObject(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		final public String getConversationId() {
			// TODO Auto-generated method stub
			return null;
		}


}
