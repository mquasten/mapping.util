package de.mq.mapping.util.json.support;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;
import de.mq.mapping.util.json.MapBasedResultBuilder;



public class SimpleMapBasedResponseClassFactoryImpl implements MapBasedResponseClassFactory {
	
	
	private Map<UUID, Class<MapBasedResponse>> classes = new HashMap<>();
	
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedClassFactory#createClass(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final Class<MapBasedResponse> createClass(final Collection<?> mappings)  {
		final UUID uuid = mappingUUID(mappings);
		if(classes.containsKey(uuid)) {
			return classes.get(uuid);
		}
		
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AbstractMapBasedResult.class);
		enhancer.setCallbackFilter(callBackFilter());
		enhancer.setCallbackTypes(new Class[] { NoOp.INSTANCE.getClass(), MethodInterceptor.class });
	
		final Class<MapBasedResponse> clazz = enhancer.createClass();
		Enhancer.registerCallbacks(clazz, callbacks(mappings));

		classes.put(uuid, clazz);
		return clazz;
	}

	private UUID mappingUUID(final Collection<?> mappings) {
		final Map<String,Integer> hashes = new TreeMap<>();
		for(final Object obj : mappings){
			Assert.isInstanceOf(Mapping.class, obj, "Not a Mappingdefinition");
			final Mapping mapping = (Mapping) obj;
			hashes.put(mapping.key(), mapping.hashCode());	
		}
		
		return UUID.nameUUIDFromBytes(SerializationUtils.serialize(hashes));
	}

	@Override
	public final MapBasedResultBuilder mappingBuilder()  {
		return new MapBasedResultBuilderImpl();
	}


	private CallbackFilter callBackFilter() {
		return new CallbackFilter() {

			@Override
			public int accept(Method method) {
				
				if( method.getName().equals("configure")) {
					return 1;
				}
				return 0;
			}};
	}
	
	private Callback[] callbacks(final Collection<?> mappings) {
		return new Callback[]  { NoOp.INSTANCE , new MethodInterceptor() {

			
			@SuppressWarnings("unchecked")
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				Assert.isInstanceOf(AbstractMapBasedResult.class, obj, "Wrong class "+ obj.getClass() + ", expected: "+ AbstractMapBasedResult.class.getName() );
				final AbstractMapBasedResult target = (AbstractMapBasedResult)obj;
				
				Assert.notEmpty(mappings);
				target.assign( (Collection<Mapping>) mappings);		
				
				
				return null;
			}}};
	}




	

}
