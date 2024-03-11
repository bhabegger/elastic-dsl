package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;
import tech.habegger.elastic.shared.CalendarUnit;
import tech.habegger.elastic.shared.RateUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAvgAggregation.avg;
import static tech.habegger.elastic.aggregation.ElasticBoxPlotAggregation.boxPlot;
import static tech.habegger.elastic.aggregation.ElasticCardinalityAggregation.cardinality;
import static tech.habegger.elastic.aggregation.ElasticCartesianBoundsAggregation.cartesianBounds;
import static tech.habegger.elastic.aggregation.ElasticCartesianCentroidAggregation.cartesianCentroid;
import static tech.habegger.elastic.aggregation.ElasticDateHistogramAggregation.dateHistogram;
import static tech.habegger.elastic.aggregation.ElasticExtendedStatsAggregation.extendedStats;
import static tech.habegger.elastic.aggregation.ElasticGeoBoundsAggregation.geoBounds;
import static tech.habegger.elastic.aggregation.ElasticGeoCentroidAggregation.geoCentroid;
import static tech.habegger.elastic.aggregation.ElasticGeoLineAggregation.geoLine;
import static tech.habegger.elastic.aggregation.ElasticIpRateAggregation.rate;
import static tech.habegger.elastic.aggregation.ElasticMatrixStatsAggregation.matrixStats;
import static tech.habegger.elastic.aggregation.ElasticMaxAggregation.max;
import static tech.habegger.elastic.aggregation.ElasticMedianAbsoluteDeviationAggregation.medianAbsoluteDeviation;
import static tech.habegger.elastic.aggregation.ElasticMinAggregation.min;
import static tech.habegger.elastic.aggregation.ElasticPercentileRanksAggregation.percentileRanks;
import static tech.habegger.elastic.aggregation.ElasticPercentilesAggregation.percentiles;
import static tech.habegger.elastic.aggregation.ElasticStatsAggregation.stats;
import static tech.habegger.elastic.aggregation.ElasticStringStatsAggregation.stringStats;
import static tech.habegger.elastic.aggregation.ElasticSumAggregation.sum;
import static tech.habegger.elastic.aggregation.ElasticTTestAggregation.FilterableFieldSpec.field;
import static tech.habegger.elastic.aggregation.ElasticTTestAggregation.TTestType.heteroscedastic;
import static tech.habegger.elastic.aggregation.ElasticTTestAggregation.TTestType.paired;
import static tech.habegger.elastic.aggregation.ElasticTTestAggregation.tTest;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
import static tech.habegger.elastic.aggregation.ElasticValueCountAggregation.valueCount;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.shared.TDigestSpec.ExecutionHint.high_accuracy;

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
    void matrixStatsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("statistics", matrixStats("poverty", "income"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "statistics": {
                      "matrix_stats": {
                        "fields": [ "poverty", "income" ]
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
    void medianAbsoluteDeviationAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("review_variability",
                medianAbsoluteDeviation("rating")
                    .withCompression(100))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "review_variability": {
                      "median_absolute_deviation": {
                        "field": "rating",
                        "compression": 100
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void percentileRanksAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_ranks",
                percentileRanks("load_time", 500.0, 600.0)
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
                    "load_time_ranks": {
                      "percentile_ranks": {
                        "field": "load_time",
                        "values": [ 500.0, 600.0 ]
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void percentilesAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_outlier",
                percentiles("load_time")
                    .withTDigest(200)
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
                    "load_time_outlier": {
                      "percentiles": {
                        "field": "load_time",
                        "tdigest": {
                          "compression": 200
                        }
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void percentilesAggregationWithExecutionHint() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_outlier",
                percentiles("load_time")
                    .withTDigest(high_accuracy)
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
                    "load_time_outlier": {
                      "percentiles": {
                        "field": "load_time",
                        "tdigest": {
                          "execution_hint": "high_accuracy"
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void percentilesAggregationWithHDR() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("load_time_outlier",
                percentiles("load_time", 95.0, 99.0, 99.9)
                    .withHdr(3)
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
                    "load_time_outlier": {
                      "percentiles": {
                        "field": "load_time",
                        "percents": [ 95.0, 99.0, 99.9 ],
                        "hdr": {
                          "number_of_significant_value_digits": 3
                        }
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void rateAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("by_date",
                dateHistogram("date", CalendarUnit.month)
                    .aggregation("my_rate", rate(RateUnit.year))
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
                    "by_date": {
                      "date_histogram": {
                        "field": "date",
                        "calendar_interval": "month"
                      },
                      "aggregations": {
                        "my_rate": {
                          "rate": {
                            "unit": "year"
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void rateAggregationWithField() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("by_date",
                dateHistogram("date", CalendarUnit.month)
                    .aggregation("avg_price", rate("price", RateUnit.day))
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
                    "by_date": {
                      "date_histogram": {
                        "field": "date",
                        "calendar_interval": "month"
                      },
                      "aggregations": {
                        "avg_price": {
                          "rate": {
                            "field": "price",
                            "unit": "day"
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void rateAggregationWithValueCount() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("by_date",
                dateHistogram("date", CalendarUnit.month)
                    .aggregation("avg_number_of_sales_per_year",
                        rate("price", RateUnit.year, ElasticIpRateAggregation.RateMode.value_count))
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
                    "by_date": {
                      "date_histogram": {
                        "field": "date",
                        "calendar_interval": "month"
                      },
                      "aggregations": {
                        "avg_number_of_sales_per_year": {
                          "rate": {
                            "field": "price",
                            "unit": "year",
                            "mode": "value_count"
                          }
                        }
                      }
                    }
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
    void stringStatsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("message_stats", stringStats("message.keyword").withShowDistribution())
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "message_stats": {
                      "string_stats": {
                        "field": "message.keyword",
                        "show_distribution": true
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void stringStatsAggregationWithMissing() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("message_stats", stringStats("message.keyword").withMissing("[empty message]"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "message_stats": {
                      "string_stats": {
                        "field": "message.keyword",
                        "missing": "[empty message]"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void tTestAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("startup_time_ttest", tTest(
                "startup_time_before",
                "startup_time_after",
                paired
            ))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "startup_time_ttest": {
                      "t_test": {
                        "a": { "field": "startup_time_before" },
                        "b": { "field": "startup_time_after" },
                        "type": "paired"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void tTestAggregationWithFilters() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("startup_time_ttest", tTest(
                field("startup_time_before").withFilter(term("group", "A")),
                field("startup_time_before").withFilter(term("group", "B")),
                heteroscedastic
            ))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "startup_time_ttest": {
                      "t_test": {
                        "a": {
                          "field": "startup_time_before",
                          "filter": {
                            "term": {
                              "group": "A"
                            }
                          }
                        },
                        "b": {
                          "field": "startup_time_before",
                          "filter": {
                            "term": {
                              "group": "B"
                            }
                          }
                        },
                        "type": "heteroscedastic"
                      }
                    }
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
