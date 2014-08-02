package de.mq.mapping.util.json.support;

import java.lang.reflect.Method;
import java.util.Collection;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.springframework.util.Assert;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;
import de.mq.mapping.util.json.MapBasedResultBuilder;



public class SimpleMapBasedResponseClassFactoryImpl implements MapBasedResponseClassFactory {
	
	
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedClassFactory#createClass(java.util.Collection)
	 */
	@Override
	public final Class<MapBasedResponse> createClass(final Collection<?> mappings) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AbstractMapBasedResult.class);
		enhancer.setCallbackFilter(callBackFilter());
		enhancer.setCallbackTypes(new Class[] { NoOp.INSTANCE.getClass(), MethodInterceptor.class });
		@SuppressWarnings("unchecked")
		final Class<MapBasedResponse> clazz = enhancer.createClass();
		Enhancer.registerCallbacks(clazz, callbacks(mappings));

		return clazz;
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
