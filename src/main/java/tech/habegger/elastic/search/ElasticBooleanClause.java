package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record ElasticBooleanClause(BooleanBody bool) implements ElasticSearchClause {
    public static Builder newBool() {
        return new Builder();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BooleanBody(
        List<ElasticSearchClause> should,
        List<ElasticSearchClause> must,
        @JsonProperty("must_not")
        List<ElasticSearchClause> mustNot,
        List<ElasticSearchClause> filter,
        @JsonProperty("minimum_should_match")
        Integer minimumShouldMatch) {
    }

    @SuppressWarnings({"UnusedReturnValue", "FieldCanBeLocal","unused"})
    public static class Builder {
        private final List<ElasticSearchClause> should = new ArrayList<>();
        private final List<ElasticSearchClause> must = new ArrayList<>();
        private final List<ElasticSearchClause> mustNot = new ArrayList<>();
        private final List<ElasticSearchClause> filter = new ArrayList<>();

        private Integer minimumShouldMatch;

        public Builder should(ElasticSearchClause clause) {
            should.add(clause);
            return this;
        }
        public Builder must(ElasticSearchClause clause) {
            must.add(clause);
            return this;
        }
        public Builder mustNot(ElasticSearchClause clause) {
            mustNot.add(clause);
            return this;
        }

        public Builder filter(ElasticSearchClause clause) {
            filter.add(clause);
            return this;
        }
        public Builder minimumShouldMatch(int minimum) {
            minimumShouldMatch = minimum;
            return this;
        }

        public ElasticSearchClause build() {
            return new ElasticBooleanClause(new BooleanBody(
                nullIfEmpty(should),
                nullIfEmpty(must),
                nullIfEmpty(mustNot),
                nullIfEmpty(filter),
                should.isEmpty() ? null : minimumShouldMatch
            ));
        }

        private static List<ElasticSearchClause> nullIfEmpty(List<ElasticSearchClause> clause) {
            if(clause.isEmpty()) {
                return null;
            } else {
                return clause;
            }
        }
    }
}
