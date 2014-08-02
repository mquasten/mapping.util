package de.mq.mapping.util.json;


import java.util.Collection;
public interface MapBasedResultBuilder {

	MapBasedResultBuilder withParentMapping(String node, String... paths);

	MapBasedResultBuilder withFieldMapping(String node, String field, String... paths);

	MapBasedResultBuilder withChildMapping(String field, String... paths);

	Collection<?> build();

}