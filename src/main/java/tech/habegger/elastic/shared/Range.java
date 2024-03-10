package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Range(Float from, Float to, String key) {
    public static Range between(Float from, Float to) {
        return new Range(from, to, null);
    }

    public static Range from(Float from) {
        return new Range(from, null, null);
    }

    public static Range to(Float to) {
        return new Range(null, to, null);
    }

    public Range withKey(String key) {
        return new Range(this.from, this.to, key);
    }
}
