package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticRegexpClause(Map<String, RegexpBody> regexp) implements ElasticSearchClause {
    public static ElasticRegexpClause regexp(String field, String value) {
        return new ElasticRegexpClause(Map.of(field, new RegexpBody(value, null, null, null, null)));
    }

    public ElasticRegexpClause withFlags(RegexpFlags... flags) {
        return withBody(original ->
                new RegexpBody(
                        original.value,
                        RegexpFlags.asString(flags),
                        original.caseInsensitive,
                        original.maxDeterminizedStates,
                        original.rewrite
                )
        );
    }

    public ElasticRegexpClause withoutCaseSensitivity() {
        return withBody(original ->
                new RegexpBody(
                        original.value,
                        original.flags,
                        true,
                        original.maxDeterminizedStates,
                        original.rewrite
                )
        );
    }

    public ElasticRegexpClause withMaxDeterminizedStates(int maxDeterminizedStates) {
        return withBody(original ->
                new RegexpBody(
                        original.value,
                        original.flags,
                        original.caseInsensitive,
                        maxDeterminizedStates,
                        original.rewrite
                )
        );
    }

    public ElasticRegexpClause withRewrite(RewriteMethod rewrite) {
        return withBody(original ->
                new RegexpBody(
                        original.value,
                        original.flags,
                        original.caseInsensitive,
                        original.maxDeterminizedStates,
                        rewrite
                )
        );
    }

    private ElasticRegexpClause withBody(Function<RegexpBody, RegexpBody> bodyChange) {
        var entry = this.regexp.entrySet().stream().findFirst().orElseThrow();
        var field = entry.getKey();
        var newBody = bodyChange.apply(entry.getValue());
        return new ElasticRegexpClause(
                Map.of(
                        field,
                        newBody
                )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RegexpBody(
            String value,
            String flags,

            @JsonProperty("case_insensitive")
            Boolean caseInsensitive,

            @JsonProperty("max_determinized_states")
            Integer maxDeterminizedStates,

            @JsonProperty("rewrite")
            RewriteMethod rewrite
    ) {
    }

    public enum RegexpFlags {
        ALL,
        COMPLEMENT,
        EMPTY,
        INTERVAL,
        INTERSECTION,
        ANYSTRING,
        NONE;

        public static String asString(RegexpFlags... flags) {
            return String.join("|", Arrays.stream(flags).map(RegexpFlags::toString).collect(Collectors.toSet()));
        }
    }
}
