package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticWildcardClause(Map<String, WildcardBody> wildcard) implements ElasticSearchClause {
    public static ElasticWildcardClause wildcard(String field, String value) {
        return new ElasticWildcardClause(Map.of(field, new WildcardBody(value, null, null, null)));
    }

    public ElasticWildcardClause withBoost(float boost) {
        return withBody(original ->
                new WildcardBody(
                        original.value,
                        boost,
                        original.caseInsensitive,
                        original.rewrite
                )
        );
    }

    public ElasticWildcardClause withoutCaseSensitivity() {
        return withBody(original ->
                new WildcardBody(
                        original.value,
                        original.boost,
                        true,
                        original.rewrite
                )
        );
    }

    public ElasticWildcardClause withRewrite(RewriteMethod rewrite) {
        return withBody(original ->
                new WildcardBody(
                        original.value,
                        original.boost,
                        original.caseInsensitive,
                        rewrite
                )
        );
    }

    private ElasticWildcardClause withBody(Function<WildcardBody, WildcardBody> bodyChange) {
        var entry = this.wildcard.entrySet().stream().findFirst().orElseThrow();
        var field = entry.getKey();
        var newBody = bodyChange.apply(entry.getValue());
        return new ElasticWildcardClause(
                Map.of(
                        field,
                        newBody
                )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record WildcardBody(
            String value,
            Float boost,
            @JsonProperty("case_insensitive")
            Boolean caseInsensitive,
            @JsonProperty("rewrite")
            RewriteMethod rewrite
    ) {
    }

}
