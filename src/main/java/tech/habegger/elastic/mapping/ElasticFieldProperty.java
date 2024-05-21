package tech.habegger.elastic.mapping;

import java.util.Map;

public record ElasticFieldProperty(
    String type,
    Map<String, ElasticFieldProperty> fields,
    Integer ignore_above,
    String format
) implements ElasticProperty {
}
