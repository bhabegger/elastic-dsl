package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SourceSpec(List<String> includes, List<String> excludes) {
    public static SourceSpec include(String... includes) {
        return new SourceSpec(List.of(includes), null);
    }
    public static SourceSpec exclude(String... excludes) {
        return new SourceSpec(null, List.of(excludes));
    }
    public static SourceSpec source(List<String> includes, List<String> excludes) {
        return new SourceSpec(includes, excludes);
    }
}
