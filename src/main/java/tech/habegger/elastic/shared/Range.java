package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Range(Double from, Double to, String key) {
    public static Range between(Double from, Double to) {
        return new Range(from, to, null);
    }

    public static Range from(Double from) {
        return new Range(from, null, null);
    }

    public static Range to(Double to) {
        return new Range(null, to, null);
    }

    public Range withKey(String key) {
        return new Range(this.from, this.to, key);
    }
}
