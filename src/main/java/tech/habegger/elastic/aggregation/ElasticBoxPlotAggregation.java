package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.ExecutionHint;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticBoxPlotAggregation extends ElasticAggregations {

    @JsonProperty("boxplot")
    private final BoxPlotBody boxplot;

    ElasticBoxPlotAggregation(
        BoxPlotBody boxplot
    ) {
        this.boxplot = boxplot;
    }
    public static ElasticBoxPlotAggregation boxPlot(String field) {
        return new ElasticBoxPlotAggregation(new BoxPlotBody(field, null, null, null));
    }

    public ElasticBoxPlotAggregation withCompression(int compression) {
        return withBody(original -> new BoxPlotBody(
            original.field,
            compression,
            original.executionHint,
            original.missing
        ));
    }

    public ElasticBoxPlotAggregation withExecutionHint(ExecutionHint executionHint) {
        return withBody(original -> new BoxPlotBody(
            original.field,
            original.compression,
            executionHint,
            original.missing
        ));
    }

    public ElasticBoxPlotAggregation withMissing(float missing) {
        return withBody(original -> new BoxPlotBody(
            original.field,
            original.compression,
            original.executionHint,
            missing
        ));
    }

    private ElasticBoxPlotAggregation withBody(Function<BoxPlotBody,BoxPlotBody> update) {
        return new ElasticBoxPlotAggregation(update.apply(this.boxplot));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record BoxPlotBody (
        @JsonProperty("field")
        String field,
        @JsonProperty("compression")
        Integer compression,
        @JsonProperty("execution_hint")
        ExecutionHint executionHint,
        @JsonProperty("missing")
        Float missing
    ) {}
}
