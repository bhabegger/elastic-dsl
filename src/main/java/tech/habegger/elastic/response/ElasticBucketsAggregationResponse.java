package tech.habegger.elastic.response;

import java.util.List;
import java.util.Map;

public record ElasticBucketsAggregationResponse(
    List<Map<String, ?>> buckets
) implements ElasticAggregationResponse {
}
