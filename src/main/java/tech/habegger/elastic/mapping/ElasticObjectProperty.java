package tech.habegger.elastic.mapping;

import java.util.Map;

public record ElasticObjectProperty(Map<String, ElasticProperty> properties) implements ElasticProperty {
}
