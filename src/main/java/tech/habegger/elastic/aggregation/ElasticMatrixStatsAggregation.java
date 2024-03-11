package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.MultiAggregationMode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMatrixStatsAggregation extends ElasticAggregations {

    @JsonProperty("matrix_stats")
    private final MatrixStatsBody matrixStats;

    ElasticMatrixStatsAggregation(
        MatrixStatsBody matrixStats
    ) {
        this.matrixStats = matrixStats;
    }

    public ElasticMatrixStatsAggregation withMode(MultiAggregationMode mode) {
        return withBody(original -> new MatrixStatsBody(
            original.fields,
            mode,
            original.missing
        ));
    }

    public ElasticMatrixStatsAggregation withMissing(Float missing) {
        return withBody(original -> new MatrixStatsBody(
            original.fields,
            original.mode,
            missing
        ));
    }

    private ElasticMatrixStatsAggregation withBody(Function<MatrixStatsBody, MatrixStatsBody> update) {
        return new ElasticMatrixStatsAggregation(update.apply(this.matrixStats));
    }

    public static ElasticMatrixStatsAggregation matrixStats(String... fields) {
        return new ElasticMatrixStatsAggregation(new MatrixStatsBody(Arrays.asList(fields), null, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record MatrixStatsBody(
        List<String> fields,
        MultiAggregationMode mode,
        Float missing) {
    }

}
