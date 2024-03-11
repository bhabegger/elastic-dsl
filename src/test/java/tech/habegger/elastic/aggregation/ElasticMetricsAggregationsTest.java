package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAvgAggregation.avg;
import static tech.habegger.elastic.aggregation.ElasticBoxPlotAggregation.boxPlot;
import static tech.habegger.elastic.aggregation.ElasticCardinalityAggregation.cardinality;
import static tech.habegger.elastic.aggregation.ElasticCartesianBoundsAggregation.cartesianBounds;
import static tech.habegger.elastic.aggregation.ElasticCartesianCentroidAggregation.cartesianCentroid;
import static tech.habegger.elastic.aggregation.ElasticExtendedStatsAggregation.extendedStats;
import static tech.habegger.elastic.aggregation.ElasticGeoBoundsAggregation.geoBounds;
import static tech.habegger.elastic.aggregation.ElasticGeoCentroidAggregation.geoCentroid;
import static tech.habegger.elastic.aggregation.ElasticGeoLineAggregation.geoLine;
import static tech.habegger.elastic.aggregation.ElasticMaxAggregation.max;
import static tech.habegger.elastic.aggregation.ElasticMinAggregation.min;
import static tech.habegger.elastic.aggregation.ElasticStatsAggregation.stats;
import static tech.habegger.elastic.aggregation.ElasticSumAggregation.sum;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
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
    void geoCentroidAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("cities",
                termsAgg("city.keyword")
                    .aggregation("centroid",
                        geoCentroid("location")
                    )
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "cities": {
                      "aggregations": {
                        "centroid": {
                          "geo_centroid": { "field": "location" }
                        }
                      },
                      "terms": { "field": "city.keyword" }
                    }
                  }
                }
                """
        );
    }


    @Test
    void geoLineAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("line",
                geoLine("my_location","@timestamp")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "line": {
                      "geo_line": {
                        "point": {"field": "my_location"},
                        "sort":  {"field": "@timestamp"}
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void cartesianBoundsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(
                match("name", "musée")
            )
            .aggregation("viewport",
                cartesianBounds("location")
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
                      "cartesian_bounds": {
                        "field": "location"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void cartesianCentroidAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("centroid",
                cartesianCentroid("location")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "centroid": {
                      "cartesian_centroid": {
                        "field": "location"
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
