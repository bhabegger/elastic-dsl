package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticFuzzyClause(Map<String, FuzzyBody> fuzzy) implements ElasticSearchClause {
    public static ElasticFuzzyClause fuzzy(String field, String value) {
        return new ElasticFuzzyClause(Map.of(field, new FuzzyBody(value, null, null, null, null, null)));
    }
    public ElasticFuzzyClause withFuzziness(int left, int right) {
        return withBody((original) ->
            new ElasticFuzzyClause.FuzzyBody(
                original.value,
                "AUTO:%d,%d".formatted(left,right),
                original.maxExpansions,
                original.prefixLength,
                original.transpositions,
                original.rewrite
            )
        );
    }

    public ElasticFuzzyClause withMaxExpansions(int maxExpansions) {
        return withBody(original ->
            new ElasticFuzzyClause.FuzzyBody(
                    original.value,
                    original.fuzziness,
                    maxExpansions,
                    original.prefixLength,
                    original.transpositions,
                    original.rewrite
            )
        );
    }
    public ElasticFuzzyClause withPrefixLength(int prefixLength) {
        return withBody(original ->
            new ElasticFuzzyClause.FuzzyBody(
                    original.value,
                    original.fuzziness,
                    original.maxExpansions,
                    prefixLength,
                    original.transpositions,
                    original.rewrite
            )
        );
    }
    public ElasticFuzzyClause withoutTranspositions() {
        return withBody(original ->
            new ElasticFuzzyClause.FuzzyBody(
                    original.value,
                    original.fuzziness,
                    original.maxExpansions,
                    original.prefixLength,
                    false,
                    original.rewrite
            )
        );
    }

    public ElasticFuzzyClause withRewrite(RewriteMethod rewrite) {
        return withBody( original ->
                new ElasticFuzzyClause.FuzzyBody(
                        original.value,
                        original.fuzziness,
                        original.maxExpansions,
                        original.prefixLength,
                        original.transpositions,
                        rewrite
                )
        );
    }

    private ElasticFuzzyClause withBody(Function<FuzzyBody, FuzzyBody> bodyChange) {
        var entry = this.fuzzy.entrySet().stream().findFirst().orElseThrow();
        var field = entry.getKey();
        var newBody = bodyChange.apply(entry.getValue());
        return new ElasticFuzzyClause(
            Map.of(
                field,
                newBody
            )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record FuzzyBody(
        String value,
        String fuzziness,

        @JsonProperty("max_expansions")
        Integer maxExpansions,

        @JsonProperty("prefix_length")
        Integer prefixLength,

        @JsonProperty("transpositions")
        Boolean transpositions,

        @JsonProperty("rewrite")
        RewriteMethod rewrite
    ) {}

}
