package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.search.ElasticSearchClause;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTTestAggregation extends ElasticAggregations {

    @JsonProperty("t_test")
    private final TTestBody tTest;

    ElasticTTestAggregation(
        TTestBody tTest
    ) {
        this.tTest = tTest;
    }
    public static ElasticTTestAggregation tTest(String a, String b, TTestType type) {
        return tTest(
            FilterableFieldSpec.field(a),
            FilterableFieldSpec.field(b),
            type
        );
    }

    public static ElasticTTestAggregation tTest(FilterableFieldSpec a, FilterableFieldSpec b, TTestType type) {
        return new ElasticTTestAggregation(new TTestBody(
            a,
            b,
            type
        ));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TTestBody(
        @JsonProperty("a")
        FilterableFieldSpec a,
        @JsonProperty("b")
        FilterableFieldSpec b,
        @JsonProperty("type")
        TTestType type
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FilterableFieldSpec(String field, ElasticSearchClause filter) {
        public static FilterableFieldSpec field(String field) {
            return new FilterableFieldSpec(field, null);
        }
        public FilterableFieldSpec withFilter(ElasticSearchClause filter) {
            return new FilterableFieldSpec(field, filter);
        }
    }

    @SuppressWarnings("unused")
    public enum TTestType {
        paired,
        homoscedastic,
        heteroscedastic
    }
}
