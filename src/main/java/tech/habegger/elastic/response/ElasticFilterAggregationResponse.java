package tech.habegger.elastic.response;

import java.util.Map;

public record ElasticFilterAggregationResponse(
    Long docCount, Map<String,
    ElasticAggregationResponse> aggregations
) implements ElasticAggregationResponse, ElasticAggregationResponseContainer {
}
