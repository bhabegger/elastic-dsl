package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAvgAggregation.avg;
import static tech.habegger.elastic.aggregation.ElasticBoxPlotAggregation.boxPlot;
import static tech.habegger.elastic.aggregation.ElasticCardinalityAggregation.cardinality;
import static tech.habegger.elastic.aggregation.ElasticExtendedStatsAggregation.extendedStats;
import static tech.habegger.elastic.aggregation.ElasticGeoBoundsAggregation.geoBounds;
import static tech.habegger.elastic.aggregation.ElasticMaxAggregation.max;
import static tech.habegger.elastic.aggregation.ElasticMinAggregation.min;
import static tech.habegger.elastic.aggregation.ElasticStatsAggregation.stats;
import static tech.habegger.elastic.aggregation.ElasticSumAggregation.sum;
import static tech.habegger.elastic.aggregation.ElasticValueCountAggregation.valueCount;
import static tech.habegger.elastic.search.ElasticMatchClause.match;

public class ElasticMetricsAggregationsTest {

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
    void boxPlotAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_boxplot", boxPlot("load_time"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "load_time_boxplot": {
                      "boxplot": {
                        "field": "load_time"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void boxPlotAggregationWithCompression() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_boxplot",
                boxPlot("load_time")
                    .withCompression(200)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "load_time_boxplot": {
                      "boxplot": {
                        "field": "load_time",
                        "compression": 200
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void cardinalityAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("type_count",
                cardinality("type")
                    .withPrecisionThreshold(100)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "type_count": {
                      "cardinality": {
                        "field": "type",
                        "precision_threshold": 100
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void extendedStatsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("grades_stats",
                extendedStats("grade")
                    .withSigma(3.0)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "grades_stats": {
                      "extended_stats": {
                        "field": "grade",
                        "sigma": 3.0
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void geoBoundsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(
                match("name", "musée")
            )
            .aggregation("viewport",
                geoBounds("location")
                    .withWrapLongitude()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "match": { "name": "musée" }
                  },
                  "aggregations": {
                    "viewport": {
                      "geo_bounds": {
                        "field": "location",
                        "wrap_longitude": true
                      }
                    }
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
