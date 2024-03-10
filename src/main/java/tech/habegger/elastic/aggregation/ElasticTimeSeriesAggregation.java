package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticTimeSeriesAggregation extends ElasticAggregations {

    @JsonProperty("time_series")
    final TimeSeriesBody timeSeries;

    ElasticTimeSeriesAggregation(TimeSeriesBody timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static ElasticTimeSeriesAggregation timeSeries(int size) {
        return new ElasticTimeSeriesAggregation( new TimeSeriesBody(size, null));
    }

    public static ElasticTimeSeriesAggregation timeSeries() {
        return new ElasticTimeSeriesAggregation( new TimeSeriesBody(null, null));
    }

    public ElasticTimeSeriesAggregation withKeyed() {
        return new ElasticTimeSeriesAggregation((new TimeSeriesBody(timeSeries.size, true)));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TimeSeriesBody(
        Integer size,
        Boolean keyed
    ) { }
}
