package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.OrderSpec;
import tech.habegger.elastic.shared.SortSpec;
import tech.habegger.elastic.shared.SourceSpec;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTopHitsAggregation extends ElasticAggregations {

    @JsonProperty("top_hits")
    private final TopHitsBody topHits;

    ElasticTopHitsAggregation(
        TopHitsBody topHits
    ) {
        this.topHits = topHits;
    }

    public ElasticTopHitsAggregation withSort(SortSpec... sort) {
        return withBody(original -> new TopHitsBody(
            original.size,
            SortSpec.toOutput(sort),
            original.source
        ));
    }

    public ElasticTopHitsAggregation withSource() {
        return withBody(original -> new TopHitsBody(
            original.size,
            original.sort,
            true
        ));
    }


    public ElasticTopHitsAggregation withSource(SourceSpec source) {
        return withBody(original -> new TopHitsBody(
            original.size,
            original.sort,
            source
        ));
    }

    private ElasticTopHitsAggregation withBody(Function<TopHitsBody, TopHitsBody> update) {
        return new ElasticTopHitsAggregation(update.apply(this.topHits));
    }

    public static ElasticTopHitsAggregation topHits(Integer size) {
        return new ElasticTopHitsAggregation(new TopHitsBody(size, null, null));
    }

    public static ElasticTopHitsAggregation topHits() {
        return new ElasticTopHitsAggregation(new TopHitsBody(null, null, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record TopHitsBody(
        Integer size,
        List<Map<String, OrderSpec>> sort,

        @JsonProperty("_source")
        Object source
    ) { }
}
