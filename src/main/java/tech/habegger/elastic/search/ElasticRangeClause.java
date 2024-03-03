package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal","unused"})
public class ElasticRangeClause implements ElasticSearchClause {

    @JsonProperty("range")
    private final Map<String, RangeBody> range;

    ElasticRangeClause(Map<String, RangeBody> range) {
        this.range = range;
    }
    public static ElasticRangeClause range(String field, String from, String to) {
        return new ElasticRangeClause(Map.of(field, new RangeBody(from, to)));
    }
    public static ElasticRangeClause range(String field, Instant from, Instant to) {
        return range(field, from == null ? null : from.toString(), to == null ? null : to.toString());
    }

    public static ElasticRangeClause range(String field, LocalDate from, LocalDate to) {
        return range(field, from == null ? null : from.toString(), to == null ? null : to.toString());
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record RangeBody(String gte, String lte) {
    }
}
