package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.search.ElasticSearchClause;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticFilterAggregation extends ElasticAggregations {

    @JsonProperty("filter")
    final ElasticSearchClause filter;

    ElasticFilterAggregation(ElasticSearchClause filter) {
        this.filter = filter;
    }

    public static ElasticFilterAggregation filter(ElasticSearchClause filter) {
        return new ElasticFilterAggregation(filter);
    }

}
