package de.mq.mapping.util.proxy.support;

import java.util.Map;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;

public interface ModelRepositoryBuilder {

	public abstract ModelRepositoryBuilder withBeanResolver(BeanResolver beanResolver);

	public abstract ModelRepositoryBuilder withDomain(Object object);

	public abstract ModelRepositoryBuilder withMap(Map<String, ? extends Object> map);

	public abstract ModelRepositoryBuilder withMapEntry(String key, Object value);

	public abstract ModelRepository build();

}