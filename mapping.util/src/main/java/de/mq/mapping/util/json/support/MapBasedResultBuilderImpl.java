package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.util.Assert;

import de.mq.mapping.util.json.MapBasedResultBuilder;
import de.mq.mapping.util.json.support.MapBasedResponse.ChildField;
import de.mq.mapping.util.json.support.MapBasedResponse.InfoField;


class MapBasedResultBuilderImpl implements MapBasedResultBuilder {
	
	private Mapping parent;
	
	private Collection<Mapping> fieldMappings = new HashSet<>();
	
	private Collection<Mapping> childMappings = new HashSet<>();
	
	MapBasedResultBuilderImpl() {
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedResultBuilder#withParentMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public MapBasedResultBuilder withParentMapping(final String node, final String ...paths ) {
		Assert.isNull(parent, "Only one Parent can be Mapped");
		parent = new Mapping(node, null, paths);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedResultBuilder#withFieldMapping(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public MapBasedResultBuilder withFieldMapping(final String node, final InfoField infoField, final String ...paths ) {
		Assert.hasText(node, "Node is mandatory");
		Assert.notNull(infoField, "Field is mandatory");
		fieldMappings.add( new Mapping(node, infoField.field(), paths));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedResultBuilder#withChildMapping(java.lang.String, java.lang.String)
	 */
	@Override
	public MapBasedResultBuilder withChildMapping(final ChildField  childField, final String ... paths){
		Assert.notNull(childField, "Field is mandatory");
		childMappings.add(new Mapping(null, childField.field(), paths));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.json.support.MapBasedResultBuilder#build()
	 */
	@Override
	public final Collection<?> build() {
		final Collection<Object>  mappings = new ArrayList<>();
		Assert.notNull(parent, "ParentMapping is missining");
		parent.assignChilds(childMappings);
		mappings.add(parent);
		mappings.addAll(fieldMappings);
		return mappings; 
	}

}
