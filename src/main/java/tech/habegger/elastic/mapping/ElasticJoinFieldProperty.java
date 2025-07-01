package tech.habegger.elastic.mapping;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record ElasticJoinFieldProperty(String type, Map<String, List<String>> relations) implements ElasticProperty {

    public static Builder joinField() {
        return new Builder();
    }

    public static class Builder {
        Map<String, List<String>> relations = new LinkedHashMap<>();

        public Builder withRelation(String parent, String... children) {
            relations.put(parent, Arrays.stream(children).toList());
            return this;
        }

        public ElasticProperty build() {
            return new ElasticJoinFieldProperty("join", relations);
        }
    }

}
