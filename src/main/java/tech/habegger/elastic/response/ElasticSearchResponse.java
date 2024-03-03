package tech.habegger.elastic.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class ElasticSearchResponse<T> {
    @JsonProperty("took")
    String took;
    @JsonProperty("timed_out")
    Boolean timed_out;
    @JsonProperty("_shards")
    ShardStats _shards;
    @JsonProperty("hits")
    InternalHits<T> hits;

    @JsonProperty("aggregations")
    Map<String, AggregationResponse> aggregations;
    
    private record ShardStats(Integer total, Integer successful, Integer skipped, Integer failed) { }

    private record InternalHits<T>(TotalStats total, Integer max_score, List<ElasticHit<T>> hits) { }

    private record TotalStats(Integer value, String relation) { }

    private record AggregationResponse(
            Long value,
            String value_as_string,
            List<Map<String, ?>> buckets,
            Long doc_count_error_upper_bound,
            Long sum_other_doc_count,
            Long count,
            Double max,
            Double sum,
            Double avg,
            Double min
    ) { }

    public List<ElasticHit<T>> getHits() {
        return hits.hits;
    }

    public long getTotalHits() {
        return hits.total().value;
    }

    public String getAggregationValueAsString(String aggregationName) {
        return aggregations.get(aggregationName).value_as_string();
    }
    public Instant getAggregationValueAsInstant(String aggregationName) {
        return Optional.ofNullable(aggregations.get(aggregationName))
                .map(AggregationResponse::value)
                .map(Instant::ofEpochMilli)
                .orElse(null);
    }

    public List<Map<String, ?>> getAggregationBuckets(String aggregationName) {
        return aggregations.get(aggregationName).buckets();
    }

}
