package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TDigestSpec(
    @JsonProperty("compression")
    Integer compression,
    @JsonProperty("execution_hint")
    ExecutionHint execution_hint
) {
    public enum ExecutionHint {
        high_accuracy
    }
}
