package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.ElasticSearchSource;
import tech.habegger.elastic.aggregation.ElasticAggregations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ElasticSearchRequest(
    ElasticSearchClause query,
    ElasticKnn knn,
    @JsonProperty("_source")
    ElasticSearchSource source,
    List<?> sort,
    Integer from,
    Integer size,
    @JsonProperty("min_score")
    Double minScore,
    Map<String, ElasticAggregations> aggregations) {
    public static ElasticSearchRequest query(ElasticSearchClause clause, int pageSize) {
        return ElasticSearchRequest.requestBuilder()
                .withQuery(clause)
                .withSize(pageSize).build()
        ;
    }

    public static ElasticSearchRequest query(ElasticSearchClause clause) {
        return ElasticSearchRequest.requestBuilder()
            .withQuery(clause)
            .build()
        ;
    }

    public static Builder requestBuilder() {
        return new Builder();
    }
    @SuppressWarnings("unused")
    public static class Builder {
        ElasticSearchClause query = null;
        ElasticKnn knn = null;
        ElasticSearchSource source = null;
        List<?> sort = null;
        Integer from = null;
        Integer size = null;

        Double minScore = null;
        Map<String, ElasticAggregations> aggregations = new HashMap<>();

        private Builder() {}

        public Builder withQuery(ElasticSearchClause query) {
            this.query = query;
            return this;
        }
        public Builder withKnn(ElasticKnn knn) {
            this.knn = knn;
            return this;
        }
        public Builder withSource(ElasticSearchSource source) {
            this.source = source;
            return this;
        }
        public Builder withSort(List<?> sort) {
            this.sort = sort;
            return this;
        }
        public Builder withSort(Object... sort) {
            this.sort = List.of(sort);
            return this;
        }

        public Builder withFrom(Integer from) {
            this.from = from;
            return this;
        }

        public Builder withSize(Integer size) {
            this.size = size;
            return this;
        }

        public Builder withAggregations(Map<String, ElasticAggregations> aggregations) {
            this.aggregations.putAll(aggregations);
            return this;
        }

        public Builder withMinScore(double minScore) {
            this.minScore = minScore;
            return this;
        }
        public ElasticSearchRequest build() {
            return new ElasticSearchRequest(query,knn,source,sort,from,size,minScore, aggregations);
        }

        public Builder aggregation(String name, ElasticAggregations aggregation) {
            this.aggregations.put(name,aggregation);
            return this;
        }
    }
}

