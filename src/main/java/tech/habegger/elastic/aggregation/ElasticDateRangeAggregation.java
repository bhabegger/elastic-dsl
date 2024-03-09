package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticDateRangeAggregation extends ElasticAggregations {
    @JsonProperty("date_range")
    private final DateRangeBody dateRange;

    ElasticDateRangeAggregation(
        @JsonProperty("date_range")
        DateRangeBody dateRange

    ) {
        this.dateRange = dateRange;
    }

    public ElasticDateRangeAggregation withBuckets(Integer buckets) {
        return withBody((original) ->
                new DateRangeBody(
                        original.field,
                        original.ranges,
                        buckets,
                        original.format,
                        original.timeZone,
                        original.missing,
                        original.keyed
                ));
    }


    public ElasticDateRangeAggregation withFormat(String format) {
        return withBody((original) ->
                new DateRangeBody(
                        original.field,
                        original.ranges,
                        original.buckets,
                        format,
                        original.timeZone,
                        original.missing,
                        original.keyed
                ));
    }

    public ElasticAggregations withTimeZone(ZoneOffset zoneOffset) {
        return withBody((original) ->
                new DateRangeBody(
                        original.field,
                        original.ranges,
                        original.buckets,
                        original.format,
                        zoneOffset.toString(),
                        original.missing,
                        original.keyed
                ));
    }
    public ElasticAggregations withTimeZone(ZoneId zoneId) {
        return withBody((original) ->
            new DateRangeBody(
                original.field,
                original.ranges,
                original.buckets,
                original.format,
                zoneId.toString(),
                original.missing,
                original.keyed
            ));
    }
    public ElasticAggregations withKeys() {
        return withBody((original) ->
                new DateRangeBody(
                        original.field,
                        original.ranges,
                        original.buckets,
                        original.format,
                        original.timeZone,
                        original.missing,
                        true
                ));
    }
    public ElasticAggregations withMissing(String defaultValue) {
        return withBody((original) ->
                new DateRangeBody(
                        original.field,
                        original.ranges,
                        original.buckets,
                        original.format,
                        original.timeZone,
                        defaultValue,
                        original.keyed
                ));
    }
    private ElasticDateRangeAggregation withBody(Function<DateRangeBody, DateRangeBody> update) {
        return new ElasticDateRangeAggregation(update.apply(this.dateRange));
    }

    public static ElasticDateRangeAggregation dateRange(String field, DateRange... ranges) {
        return new ElasticDateRangeAggregation(
                new DateRangeBody(field, Arrays.asList(ranges),  null, null, null, null, null)
        );
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record DateRangeBody(
            @JsonProperty("field")
            String field,
            @JsonProperty("ranges")
            List<DateRange> ranges,
            @JsonProperty("buckets")
            Integer buckets,
            @JsonProperty("format")
            String format,
            @JsonProperty("time_zone")
            String timeZone,
            @JsonProperty("missing")
            String missing,
            @JsonProperty("keyed")
            Boolean keyed
    ) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DateRange(String from, String to, String key) {
        public static DateRange between(String from, String to) {
            return new DateRange(from, to, null);
        }
        public static DateRange since(String from) {
            return new DateRange(from, null, null);
        }
        public static DateRange until(String to) {
            return new DateRange(null, to, null);
        }

        public DateRange withKey(String key) {
            return new DateRange(this.from, this.to, key);
        }
    }
}
