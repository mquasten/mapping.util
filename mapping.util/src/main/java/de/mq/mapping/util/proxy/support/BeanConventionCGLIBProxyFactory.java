package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.ModelRepository;







import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import net.sf.cglib.proxy.NoOp;

@Component
@Profile({"CGLib-Proxy"})
public class BeanConventionCGLIBProxyFactory  extends AbstractBeanConventionProxyFactory{

	private Map<Class<?>, Factory> proxies = new HashMap<Class<?>, Factory>();
	


    @SuppressWarnings("unchecked")
	@Override
	public  <Web> Web createProxy(final Class<? extends Web> targetClass, final ModelRepository modelRepository ) {
    	if( modelRepository.beanResolver() != null){
			modelRepository.beanResolver().put(AOProxyFactory.class, this);
		}

		if (!proxies.containsKey(targetClass)) {
			return  (Web) add2Mapp(targetClass, modelRepository);
		}
		
	

		return (Web) proxies.get(targetClass).newInstance(newCallBacks(modelRepository));

	}

	private synchronized Object add2Mapp(final Class<?> targetClass, final ModelRepository modelRepository) {
		if (!proxies.containsKey(targetClass)) {
			
			
			final Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(targetClass);
			enhancer.setCallbackFilter(createCallBackfilter());

          
			enhancer.setCallbacks(newCallBacks(modelRepository));
			proxies.put(targetClass, (Factory) enhancer.create());
		}
		
		
		return proxies.get(targetClass);
	}

	private CallbackFilter createCallBackfilter() {
		return new CallbackFilter() {
			@Override
			public int accept(final Method method) {
				
				return index(method, numberOfInterceptors());
				
			}

			
		};
	}
	
	

	private Callback[] newCallBacks(final ModelRepository modelRepository) {
		
		final Callback[] callbacks = new Callback[numberOfInterceptors()+1];
		
		for (int i=0; i< numberOfInterceptors(); i++) {
			final int index = i;
			
			callbacks[i]=new MethodInterceptor() {
				
				@Override
				public final Object intercept(final Object object, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
				    modelRepository.assignProxy(object);
					return interceptor(index, modelRepository).invoke(method, args);
					
				}
			};
		
		}
		callbacks[numberOfInterceptors()]=NoOp.INSTANCE;
		return callbacks;
	}

	
	





	





	

}
