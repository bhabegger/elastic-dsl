package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
record StatsBody(String field, Integer missing) {
}
