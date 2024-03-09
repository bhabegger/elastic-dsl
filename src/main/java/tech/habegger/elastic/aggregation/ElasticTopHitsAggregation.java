package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTopHitsAggregation extends ElasticAggregations {

    @JsonProperty("top_hits")
    private final TopHitsBody topHits;

    ElasticTopHitsAggregation(
        TopHitsBody topHits
    ) {
        super(null);
        this.topHits = topHits;
    }
    public static ElasticAggregations topHitsAggregation(Integer size) {
        return new ElasticTopHitsAggregation(new TopHitsBody(size, false));
    }

    private record TopHitsBody(Integer size, boolean _source) {
    }
}
