package de.mq.mapping.util.proxy.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.ModelRepository;


@Component
@Profile({"Dynamic-Proxy"})
public class BeanConventionDynamicProxyFactory extends AbstractBeanConventionProxyFactory {

	@SuppressWarnings("unchecked")
	@Override()
	public final <Web> Web createProxy(final Class<? extends Web> targetClass, final  ModelRepository modelRepository) {
		if( modelRepository.beanResolver() != null){
		   modelRepository.beanResolver().put(AOProxyFactory.class, this);
		}
		return  (Web) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, new InvocationHandler() {
			
			@Override
			public Object invoke(final Object object, final Method method, final Object[] args) throws Throwable {
				
				
				final Integer index = index(method, null);
			
				if( index == null){
					return method.invoke(Proxy.getInvocationHandler(object), args);
				}
				
				
				return interceptor(index, modelRepository).invoke(method, args);
				
			}
		});
		
	}

	

	
		


	



	

	

	
	
}
