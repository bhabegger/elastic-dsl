package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticObjectProperty(Map<String, ElasticProperty> properties) implements ElasticProperty {

    public static Builder objectProperty() {
        return new Builder();
    }

    public static class Builder {
        Map<String, ElasticProperty> properties = new LinkedHashMap<>();

        public Builder withProperty(String name, ElasticProperty property) {
            properties.put(name, property);
            return this;
        }

        public ElasticObjectProperty build() {
            return new ElasticObjectProperty(properties);
        }
    }

}