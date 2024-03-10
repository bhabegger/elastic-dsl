package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAvgAggregation.avg;
import static tech.habegger.elastic.aggregation.ElasticMaxAggregation.max;
import static tech.habegger.elastic.aggregation.ElasticMinAggregation.min;
import static tech.habegger.elastic.aggregation.ElasticStatsAggregation.stats;
import static tech.habegger.elastic.aggregation.ElasticSumAggregation.sum;
import static tech.habegger.elastic.aggregation.ElasticValueCountAggregation.valueCount;
import static tech.habegger.elastic.search.ElasticMatchClause.match;

public class ElasticMetricsAggregationsTest {
    @Test
    void sumAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(match("type", "hat"))
            .aggregation("hat_prices", sum("price"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "match": { "type": "hat" }
                  },
                  "aggregations": {
                    "hat_prices": { "sum": { "field": "price" } }
                  }
                }
                """
        );
    }

    @Test
    void avgAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("avg_grade", avg("grade"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "avg_grade": { "avg": { "field": "grade" } }
                  }
                }
                """
        );
    }

    @Test
    void maxAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("max_price", max("price"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "max_price": { "max": { "field": "price" } }
                  }
                }
                """
        );
    }

    @Test
    void minAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("min_price", min("price"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "min_price": { "min": { "field": "price" } }
                  }
                }
                """
        );
    }

    @Test
    void statsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("grades_stats", stats("grade"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "grades_stats": { "stats": { "field": "grade" } }
                  }
                }
                """
        );
    }
    @Test
    void valueCountAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("types_count", valueCount("type"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations" : {
                    "types_count" : { "value_count" : { "field" : "type" } }
                  }
                }
                """
        );
    }
}
