package tech.habegger.elastic.analysis.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ElasticFilterDefinition {
    @JsonProperty("type")
    String type();

    static ElasticFilterDefinition filter(String type, Map<String, String> definition) {
        return new GenericFilter(type, definition);
    }

    class GenericFilter extends LinkedHashMap<String, Object> implements ElasticFilterDefinition  {

        public GenericFilter(String type, Map<String, String> definition) {
            this.put("type", type);
            this.putAll(definition);
        }

        @Override
        public String type() {
            return (String)this.get("type");
        }
    }
}
