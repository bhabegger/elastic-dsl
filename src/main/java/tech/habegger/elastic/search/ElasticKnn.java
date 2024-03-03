package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticKnn(String field, float[] query_vector, int k, int num_candidates, ElasticSearchClause filter) {
    public static ElasticKnn knn(String field, float[] query_vector, int k, int num_candidates, ElasticSearchClause filter) {
        return new ElasticKnn(field, query_vector, k, num_candidates, filter);
    }
}
