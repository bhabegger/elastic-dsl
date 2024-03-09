package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.search.ElasticSearchClause;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticAdjacencyMatrixAggregation extends ElasticAggregations {
    @JsonProperty("adjacency_matrix")
    private final AdjacencyMatrixBody adjacencyMatrix;

    ElasticAdjacencyMatrixAggregation(
        @JsonProperty("adjacency_matrix")
        AdjacencyMatrixBody adjacencyMatrix
    ) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public static Builder newAdjacencyMatrix() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, ElasticSearchClause> filters = new HashMap<>();

        public ElasticAdjacencyMatrixAggregation build() {
            return new ElasticAdjacencyMatrixAggregation(new AdjacencyMatrixBody(filters));
        }
        public Builder filter(String name, ElasticSearchClause clause) {
            this.filters.put(name, clause);
            return this;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record AdjacencyMatrixBody(Map<String, ElasticSearchClause> filters) {
    }
}
