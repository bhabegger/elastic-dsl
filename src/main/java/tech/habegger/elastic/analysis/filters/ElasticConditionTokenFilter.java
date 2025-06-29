package tech.habegger.elastic.analysis.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.ScriptExpression;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticConditionTokenFilter(ScriptExpression script) implements ElasticFilterDefinition {
    public static ElasticConditionTokenFilter conditionFilterDefinition(ScriptExpression script) {
        return new ElasticConditionTokenFilter(script);
    }

    @Override
    @JsonProperty("type")
    public String type() {
        return "condition";
    }
}
