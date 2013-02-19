package de.mq.mapping.util.proxy.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;

public class ModelRepositoryBuilderImpl implements ModelRepositoryBuilder {

	private  BeanResolver beanResolver;
	
	private final Set<Object> domains  = new HashSet<>();
	
	private  final  Map<String,Object> map = new HashMap<>();
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ModelRepositoryBuilder#withBeanResolver(de.mq.mapping.util.proxy.BeanResolver)
	 */
	@Override
	public final ModelRepositoryBuilder withBeanResolver(final BeanResolver beanResolver) {
		 this.beanResolver=beanResolver;
		 return this;
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ModelRepositoryBuilder#withDomain(java.lang.Object)
	 */
	@Override
	public final ModelRepositoryBuilder withDomain(final Object object) {
		domains.remove(object);
		domains.add(object);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ModelRepositoryBuilder#withMap(java.util.Map)
	 */
	@Override
	public final ModelRepositoryBuilder withMap(final Map<String, ? extends Object>  map){
		this.map.putAll(map);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ModelRepositoryBuilder#withMapEntry(java.lang.String, java.lang.Object)
	 */
	@Override
	public final ModelRepositoryBuilder withMapEntry(final String key, final Object value ){
		this.map.put(key, value);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ModelRepositoryBuilder#build()
	 */
	@Override
	public final  ModelRepository build() {
		if( beanResolver==null){
			throw new IllegalArgumentException("Beanresolver is mandatory");
		}
		return new ModelRepositoryImpl(beanResolver, map,domains.toArray(new Object[domains.size()]));
	}
	
}
