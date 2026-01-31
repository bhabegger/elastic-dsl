package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import tech.habegger.elastic.mapping.ElasticFieldProperty;
import tech.habegger.elastic.shared.OrderSpec;
import tech.habegger.elastic.shared.SortSpec;
import tech.habegger.elastic.shared.SourceSpec;
import tech.habegger.elastic.aggregation.ElasticAggregations;

import java.util.*;

import static tech.habegger.elastic.shared.Helpers.nullIfEmpty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ElasticSearchRequest(
    @JsonProperty("runtime_mappings")
    Map<String, ElasticFieldProperty> runtimeMappings,
    ElasticSearchClause query,
    ElasticKnn knn,
    @JsonProperty("_source")
    SourceSpec source,
    List<Map<String, OrderSpec>> sort,
    Integer from,
    Integer size,
    @JsonProperty("min_score")
    Double minScore,
    @JsonPropertyOrder(alphabetic = true)
    Map<String, ElasticAggregations> aggregations,
    @JsonProperty("post_filter")
    ElasticSearchClause postFilter,
    List<ElasticSearchField> fields,
    @JsonProperty("track_total_hits")
    Boolean trackTotalHits
    ) {
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
        SourceSpec source = null;
        List<SortSpec> sort = null;
        Integer from = null;
        Integer size = null;

        Double minScore = null;
        Map<String, ElasticAggregations> aggregations = new HashMap<>();
        Map<String, ElasticFieldProperty> runtimeMapping = new HashMap<>();

        ElasticSearchClause postFilter = null;
        List<ElasticSearchField> fields = new ArrayList<>();

        Boolean trackTotalHits = null;

        private Builder() {}

        public Builder withQuery(ElasticSearchClause query) {
            this.query = query;
            return this;
        }
        public Builder withKnn(ElasticKnn knn) {
            this.knn = knn;
            return this;
        }
        public Builder withSource(SourceSpec source) {
            this.source = source;
            return this;
        }

        public Builder withSort(String... sort) {
            this.sort = Arrays.stream(sort).map(SortSpec::sort).toList();
            return this;
        }
        public Builder withSort(List<SortSpec> sort) {
            this.sort = sort;
            return this;
        }
        public Builder withSort(SortSpec... sort) {
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

        public Builder withField(String... field) {
            Arrays.stream(field).forEach(f -> fields.add(new ElasticSearchField(f)));
            return this;
        }

        public Builder withFields(String... fields) {
            return withField(fields);
        }

        public Builder withPostFilter(ElasticSearchClause filter) {
            this.postFilter = filter;
            return this;
        }

        public Builder withTrackTotalHits(boolean trackTotalHits) {
            this.trackTotalHits = trackTotalHits;
            return this;
        }

        public ElasticSearchRequest build() {
            return new ElasticSearchRequest(
                nullIfEmpty(runtimeMapping),
                query,
                knn,
                source,
                SortSpec.toOutput(sort),
                from,
                size,
                minScore,
                nullIfEmpty(aggregations),
                postFilter,
                nullIfEmpty(fields),
                trackTotalHits
            );
        }

        public Builder aggregation(String name, ElasticAggregations aggregation) {
            this.aggregations.put(name,aggregation);
            return this;
        }

        public Builder withRuntimeMapping(String field, ElasticFieldProperty mapping) {
            this.runtimeMapping.put(field, mapping);
            return this;
        }
    }
}

