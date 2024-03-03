package tech.habegger.elastic;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticSearchSource(List<String> includes, List<String> excludes) {
    public static ElasticSearchSource include(String... includes) {
        return new ElasticSearchSource(List.of(includes), null);
    }
}
