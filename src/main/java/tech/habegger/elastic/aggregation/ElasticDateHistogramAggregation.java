package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.CalendarUnit;
import tech.habegger.elastic.shared.TimeUnit;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticDateHistogramAggregation extends ElasticAggregations {
    @JsonProperty("date_histogram")
    private final DateHistogramBody dateHistogram;

    ElasticDateHistogramAggregation(
        @JsonProperty("date_histogram")
        DateHistogramBody dateHistogram

    ) {
        this.dateHistogram = dateHistogram;
    }

    public ElasticDateHistogramAggregation withBuckets(Integer buckets) {
        return withBody((original) ->
                new DateHistogramBody(
                        original.field,
                        original.calendarInterval,
                        original.fixedInterval,
                        buckets,
                        original.format,
                        original.timeZone,
                        original.missing
                ));
    }


    public ElasticDateHistogramAggregation withFormat(String format) {
        return withBody((original) ->
                new DateHistogramBody(
                        original.field,
                        original.calendarInterval,
                        original.fixedInterval,
                        original.buckets,
                        format,
                        original.timeZone,
                        original.missing
                ));
    }

    public ElasticAggregations withTimeZone(ZoneOffset zoneOffset) {
        return withBody((original) ->
                new DateHistogramBody(
                        original.field,
                        original.calendarInterval,
                        original.fixedInterval,
                        original.buckets,
                        original.format,
                        zoneOffset.toString(),
                        original.missing
                ));
    }
    public ElasticAggregations withTimeZone(ZoneId zoneId) {
        return withBody((original) ->
            new DateHistogramBody(
                original.field,
                original.calendarInterval,
                original.fixedInterval,
                original.buckets,
                original.format,
                zoneId.toString(),
                original.missing
            ));
    }

    public ElasticAggregations withMissing(String defaultValue) {
        return withBody((original) ->
                new DateHistogramBody(
                        original.field,
                        original.calendarInterval,
                        original.fixedInterval,
                        original.buckets,
                        original.format,
                        original.timeZone,
                        defaultValue
                ));
    }
    private ElasticDateHistogramAggregation withBody(Function<DateHistogramBody, DateHistogramBody> update) {
        return new ElasticDateHistogramAggregation(update.apply(this.dateHistogram));
    }

    private static ElasticDateHistogramAggregation dateHistogram(String field, String calendarInterval, String fixedInterval) {
        return new ElasticDateHistogramAggregation(
            new DateHistogramBody(field, calendarInterval, fixedInterval,  null, null, null, null)
        );
    }
    public static ElasticDateHistogramAggregation dateHistogram(String field, CalendarUnit unit) {
        return dateHistogram(field, unit.name(), null);
    }

    public static ElasticDateHistogramAggregation dateHistogram(String field, Integer amount, TimeUnit unit) {
        return dateHistogram(field, null, unit.quantity(amount));
    }

    public static ElasticDateHistogramAggregation dateHistogram(String field, TimeUnit unit) {
        return dateHistogram(field, null, unit.name());
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record DateHistogramBody(
            @JsonProperty("field")
            String field,
            @JsonProperty("calendar_interval")
            String calendarInterval,
            @JsonProperty("fixed_interval")
            String fixedInterval,
            @JsonProperty("buckets")
            Integer buckets,
            @JsonProperty("format")
            String format,
            @JsonProperty("time_zone")
            String timeZone,
            @JsonProperty("missing")
            String missing
    ) { }

}
