package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HDRSpec(
    @JsonProperty("number_of_significant_value_digits")
    Integer numberOfSignificantValueDigits
) {
}
