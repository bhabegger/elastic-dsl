package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.ScriptExpression;

import java.util.function.Function;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticScriptScoreClause(ScriptScoreBody script_score) implements ElasticSearchClause {
    public static ElasticScriptScoreClause scriptScore(ElasticSearchClause query, ScriptExpression script) {
        return new ElasticScriptScoreClause(new ScriptScoreBody(query, script, null, null));
    }
    public ElasticScriptScoreClause withMinScore(Float minScore) {
        return this.withBody(original ->
            new ScriptScoreBody(
                original.query,
                original.script,
                minScore,
                original.boost
            )
        );
    }
    public ElasticScriptScoreClause withBoost(Float boost) {
        return this.withBody(original ->
            new ScriptScoreBody(
                original.query,
                original.script,
                original.minScore,
                boost
            )
        );
    }

    private ElasticScriptScoreClause withBody(Function<ScriptScoreBody, ScriptScoreBody> update) {
        return new ElasticScriptScoreClause(update.apply(script_score));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ScriptScoreBody(
        @JsonProperty("query")
        ElasticSearchClause query,
        @JsonProperty("script")
        ScriptExpression script,
        @JsonProperty("min_score")
        Float minScore,
        @JsonProperty("boost")
        Float boost
    ) {}
}
