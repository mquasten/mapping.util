package de.mq.mapping.util.json;

import java.util.Collection;

import de.mq.mapping.util.json.support.MapBasedResponse;



public interface MapBasedResponseClassFactory {

    Class<MapBasedResponse> createClass(final Collection<?> mappings);

    MapBasedResultBuilder mappingBuilder();

}