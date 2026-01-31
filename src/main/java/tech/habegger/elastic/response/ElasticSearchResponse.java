package tech.habegger.elastic.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ElasticSearchResponse<T> implements ElasticAggregationResponseContainer {
    @JsonProperty("took")
    String took;
    @JsonProperty("timed_out")
    Boolean timed_out;
    @JsonProperty("_shards")
    ShardStats _shards;
    @JsonProperty("hits")
    InternalHits<T> hits;
    @JsonProperty("aggregations")
    Map<String, ElasticAggregationResponse> aggregations;
    @JsonProperty("status")
    Integer status;
    @JsonProperty("error")
    ElasticSearchError error;

    private record ShardStats(Integer total, Integer successful, Integer skipped, Integer failed) { }

    private record InternalHits<T>(TotalStats total, Integer max_score, List<ElasticHit<T>> hits) { }

    private record TotalStats(Integer value, String relation) { }

    public List<ElasticHit<T>> getHits() {
        return hits == null ? null : hits.hits;
    }

    public long getTotalHits() {
        return hits == null ? 0 : hits.total().value;
    }

    public boolean isHitCountExact() {
        return hits != null && "eq".equals(hits.total().relation);
    }

    public String getAggregationValueAsString(String aggregationName) {
        var agg = aggregations.get(aggregationName);
        if(agg instanceof ElasticMetricsAggregationResponse metricAgg) {
            return metricAgg.value_as_string();
        }
        return null;
    }
    public Instant getAggregationValueAsInstant(String aggregationName) {
        var agg = aggregations.get(aggregationName);
        if(agg instanceof ElasticMetricsAggregationResponse metricAgg) {
            var rawValue = metricAgg.value();
            if(rawValue instanceof Long longValue) {
                return Instant.ofEpochMilli(longValue);
            }
        }
        return null;
    }

    public List<Map<String, ?>> getAggregationBuckets(String aggregationName) {
        var agg = aggregations.get(aggregationName);
        if(agg instanceof ElasticBucketsAggregationResponse bucketAgg) {
            return bucketAgg.buckets();
        } else {
            return null;
        }
    }

    public Map<String, ElasticAggregationResponse> getAggregations() {
        return aggregations;
    }

    public boolean hasError() {
        return error != null;
    }

    public ElasticSearchError getError() {
        return error;
    }

    public Integer getStatus() {
        return status;
    }
}
