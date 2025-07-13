package tech.habegger.elastic.response;

import java.util.Map;

public interface ElasticAggregationResponseContainer {
    default Map<String, ElasticAggregationResponse> getAggregations() {
        return aggregations();
    }

    default Map<String, ElasticAggregationResponse> aggregations() {
        return getAggregations();
    }

    default <U> U getAggregation(String name, Class<U> clazz) {
        var agg = getAggregations().get(name);
        if(agg != null && clazz.isAssignableFrom(agg.getClass())) {
            //noinspection unchecked
            return (U) agg;
        }
        return null;
    }
}
