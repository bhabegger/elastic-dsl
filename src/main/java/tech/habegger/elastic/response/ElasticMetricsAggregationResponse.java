package tech.habegger.elastic.response;

public record ElasticMetricsAggregationResponse(
        Number value,
        String value_as_string,
        Long doc_count_error_upper_bound,
        Long sum_other_doc_count,
        Long count,
        Double max,
        Double sum,
        Double avg,
        Double min
) implements ElasticAggregationResponse
{
    Double doubleValue() {
        if(value instanceof Double doubleValue) {
            return  doubleValue;
        }
        return null;
    }
}
