package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.*;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticHistogramAggregation extends ElasticAggregations {
    @JsonProperty("histogram")
    private final HistogramBody histogram;

    ElasticHistogramAggregation(
        @JsonProperty("histogram")
        HistogramBody histogram
    ) {
        this.histogram = histogram;
    }

    public ElasticHistogramAggregation withOffset(int offset) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                original.extendedBounds,
                original.hardBounds,
                original.order,
                original.missing,
                original.keyed
            ));
    }
    public ElasticHistogramAggregation withMinDocCount(int minDocCount) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                minDocCount,
                original.extendedBounds,
                original.hardBounds,
                original.order,
                original.missing,
                original.keyed
            ));
    }

    public ElasticHistogramAggregation withExtendedBounds(float min, float max) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                new BoundsSpec(min, max),
                original.hardBounds,
                original.order,
                original.missing,
                original.keyed
            ));
    }

    public ElasticHistogramAggregation withHardBounds(float min, float max) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                original.extendedBounds,
                new BoundsSpec(min, max),
                original.order,
                original.missing,
                original.keyed
            ));
    }

    public ElasticHistogramAggregation withOrder(String field, OrderDirection direction) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                original.extendedBounds,
                original.hardBounds,
                Map.of(field, direction),
                original.missing,
                original.keyed
            ));
    }
    public ElasticHistogramAggregation withMissing(Float missing) {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                original.extendedBounds,
                original.hardBounds,
                original.order,
                missing,
                original.keyed
            ));
    }
    public ElasticHistogramAggregation withKeyed() {
        return withBody((original) ->
            new HistogramBody(
                original.field,
                original.interval,
                original.offset,
                original.minDocCount,
                original.extendedBounds,
                original.hardBounds,
                original.order,
                original.missing,
                true
            ));
    }

    private ElasticHistogramAggregation withBody(Function<HistogramBody, HistogramBody> update) {
        return new ElasticHistogramAggregation(update.apply(this.histogram));
    }

    public static ElasticHistogramAggregation histogram(String field, Float interval) {
        return new ElasticHistogramAggregation(
            new HistogramBody(field, interval,
                null, null, null, null, null, null, null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record HistogramBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("interval")
        Float interval,
        @JsonProperty("offset")
        Float offset,
        @JsonProperty("min_doc_count")
        Integer minDocCount,
        @JsonProperty("extended_bounds")
        BoundsSpec extendedBounds,
        @JsonProperty("hard_bounds")
        BoundsSpec hardBounds,
        @JsonProperty("order")
        Map<String, OrderDirection> order,
        @JsonProperty("missing")
        Float missing,
        @JsonProperty("keyed")
        Boolean keyed
    ) {
    }

}
