package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.ScriptExpression;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticTermsSetClause(
        @JsonProperty("terms_set")
        Map<String, TermsSetBody> termsSet
) implements ElasticSearchClause {
    public static ElasticTermsSetClause termsSet(String field, String minimumShouldMatchField, String... terms) {
        return new ElasticTermsSetClause(Map.of(field, new TermsSetBody(Arrays.asList(terms), minimumShouldMatchField, null, null)));
    }

    public static ElasticTermsSetClause termsSet(String field, ScriptExpression minimumShouldMatchScript, String... terms) {
        return new ElasticTermsSetClause(Map.of(field, new TermsSetBody(Arrays.asList(terms), null, minimumShouldMatchScript, null)));
    }

    public ElasticTermsSetClause withBoost(float boost) {
        return withBody((original) -> new TermsSetBody(
            original.terms,
            original.minimumShouldMatchField,
            original.minimumShouldMatchScript,
            boost
        ));
    }

    private ElasticTermsSetClause withBody(Function<TermsSetBody, TermsSetBody> bodyChange) {
        var entry = this.termsSet.entrySet().stream().findFirst().orElseThrow();
        var field = entry.getKey();
        var newBody = bodyChange.apply(entry.getValue());
        return new ElasticTermsSetClause(
                Map.of(
                        field,
                        newBody
                )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record TermsSetBody(
            List<String> terms,
            @JsonProperty("minimum_should_match_field")
            String minimumShouldMatchField,

            @JsonProperty("minimum_should_match_script")
            ScriptExpression minimumShouldMatchScript,

            @JsonProperty("boost")
            Float boost

    ) {
    }
}
