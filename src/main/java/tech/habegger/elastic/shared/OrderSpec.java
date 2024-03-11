package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderSpec(OrderDirection order) {
}
