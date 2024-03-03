package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTopHitsAggregation extends ElasticAggregations {
    private final TopHitsBody top_hits;

    ElasticTopHitsAggregation(
            TopHitsBody top_hits
    ) {
        super(null);
        this.top_hits = top_hits;
    }
    public static ElasticAggregations topHitsAggregation(Integer size) {
        return new ElasticTopHitsAggregation(new TopHitsBody(size, false));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticTopHitsAggregation) obj;
        return Objects.equals(this.top_hits, that.top_hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(top_hits);
    }

    private record TopHitsBody(Integer size, boolean _source) {
    }
}
