package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticObjectProperty(String type, Map<String, ElasticProperty> properties) implements ElasticProperty {

    public static Builder objectProperty() {
        return new Builder(null);
    }

    public static Builder nestedObjectProperty() {
        return new Builder("nested");
    }


    public static class Builder {
        String type;
        Map<String, ElasticProperty> properties = new LinkedHashMap<>();

        public Builder(String type) {
            this.type = type;
        }

        public Builder withProperty(String name, ElasticProperty property) {
            properties.put(name, property);
            return this;
        }

        public ElasticObjectProperty build() {
            return new ElasticObjectProperty(type, properties);
        }
    }

}