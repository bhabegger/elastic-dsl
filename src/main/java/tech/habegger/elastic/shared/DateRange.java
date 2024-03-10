package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DateRange(String from, String to, String key) {
    public static DateRange between(String from, String to) {
        return new DateRange(from, to, null);
    }

    public static DateRange since(String from) {
        return new DateRange(from, null, null);
    }

    public static DateRange until(String to) {
        return new DateRange(null, to, null);
    }

    public DateRange withKey(String key) {
        return new DateRange(this.from, this.to, key);
    }
}
