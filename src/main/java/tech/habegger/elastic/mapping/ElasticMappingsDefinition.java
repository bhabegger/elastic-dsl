package tech.habegger.elastic.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

public record ElasticMappingsDefinition(
    Map<String, ElasticProperty> properties
) {

    public static Builder mappings() {
        return new Builder();
    }

    public static class Builder {
        Map<String, ElasticProperty> properties = new LinkedHashMap<>();

        public Builder withProperty(String name, ElasticProperty property) {
            this.properties.put(name, property);
            return this;
        }

        public ElasticMappingsDefinition build() {
            return new ElasticMappingsDefinition(properties);
        }
    }
}
