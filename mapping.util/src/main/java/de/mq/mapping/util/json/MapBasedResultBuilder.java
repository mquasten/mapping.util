package de.mq.mapping.util.json;


import java.util.Collection;

import de.mq.mapping.util.json.support.MapBasedResponse;
public interface MapBasedResultBuilder {

	MapBasedResultBuilder withParentMapping(final String node, final String... paths);

	MapBasedResultBuilder withFieldMapping(final String node, final MapBasedResponse.InfoField fieldInfo, final String... paths);

	MapBasedResultBuilder withChildMapping(final MapBasedResponse.ChildField field, final String... paths);

	Collection<?> build();

}