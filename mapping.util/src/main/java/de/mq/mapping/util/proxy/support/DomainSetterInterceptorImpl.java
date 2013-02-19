package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import de.mq.mapping.util.proxy.ModelRepository;


class DomainSetterInterceptorImpl implements Interceptor{

	private ModelRepository modelRepository;
	
	public DomainSetterInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}
	
	
	@Override
	public final Object invoke(final Method method, final Object[] args) throws Throwable {
		
      setterArgumentGuard(args);
      modelRepository.put(args[0]);
		return null;
	}

	private void setterArgumentGuard(final Object[] args) {
		if( args.length != 1){
      	throw new IllegalArgumentException("Setter should have 1 argument");
      }
	}

	
	

}
