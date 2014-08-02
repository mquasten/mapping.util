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



public class SimpleMapBasedClassFactoryImpl {
	
	
	
	public final Class<MapBasedResponse> createClass(final Collection<Mapping<MapBasedResultRow>> mappings) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AbstractMapBasedResult.class);
		enhancer.setCallbackFilter(callBackFilter());
		enhancer.setCallbackTypes(new Class[] { NoOp.INSTANCE.getClass(), MethodInterceptor.class });
		@SuppressWarnings("unchecked")
		final Class<MapBasedResponse> clazz = enhancer.createClass();
		Enhancer.registerCallbacks(clazz, callbacks(mappings));

		return clazz;
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
	
	private Callback[] callbacks(final Collection<Mapping<MapBasedResultRow>> mappings) {
		return new Callback[]  { NoOp.INSTANCE , new MethodInterceptor() {

			
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				Assert.isInstanceOf(AbstractMapBasedResult.class, obj, "Wrong class "+ obj.getClass() + ", expected: "+ AbstractMapBasedResult.class.getName() );
				final AbstractMapBasedResult target = (AbstractMapBasedResult)obj;
				target.assignRowClass(SimpleMapBasedResultRowImpl.class);
				Assert.notEmpty(mappings);
				target.assignMappings(mappings);		
				
				
				return null;
			}}};
	}

}
