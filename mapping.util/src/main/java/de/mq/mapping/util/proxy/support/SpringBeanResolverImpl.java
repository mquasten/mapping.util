package de.mq.mapping.util.proxy.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.BeanResolver;

@Component()
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class SpringBeanResolverImpl implements BeanResolver{
	
	private final ApplicationContext applicationContext;
	
	@Autowired
	public SpringBeanResolverImpl(final ApplicationContext applicationContext) {
		this.applicationContext=applicationContext;
	}

	@Override
	public <T> T getBeanOfType(Class<? extends T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public void put(Class<?> clazz, Object bean) {
	    
	}

}
