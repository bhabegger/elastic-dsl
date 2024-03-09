package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;
import tech.habegger.elastic.shared.CalendarUnit;
import tech.habegger.elastic.shared.TimeUnit;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAdjacencyMatrixAggregation.newAdjacencyMatrix;
import static tech.habegger.elastic.aggregation.ElasticAutoDateHistogramAggregation.autoDateHistogram;
import static tech.habegger.elastic.aggregation.ElasticCategorizeTextAggregation.categorizeText;
import static tech.habegger.elastic.aggregation.ElasticDateHistogramAggregation.dateHistogram;
import static tech.habegger.elastic.aggregation.ElasticDateRangeAggregation.DateRange.between;
import static tech.habegger.elastic.aggregation.ElasticDateRangeAggregation.DateRange.since;
import static tech.habegger.elastic.aggregation.ElasticDateRangeAggregation.DateRange.until;
import static tech.habegger.elastic.aggregation.ElasticDateRangeAggregation.dateRange;
import static tech.habegger.elastic.aggregation.ElasticDiversifiedSamplerAggregation.diversifiedSampler;
import static tech.habegger.elastic.aggregation.ElasticSignificantTermsAggregation.significantTerms;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;
import static tech.habegger.elastic.shared.DateTimeUnit.minute;

public class ElasticBucketAggregationsTest {
    @Test
    void adjacencyMatrixAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .withSize(0)
                .aggregation("interactions",
                        newAdjacencyMatrix()
                                .filter("grpA", terms("accounts", "hillary", "sidney"))
                                .filter("grpB", terms("accounts", "donald", "mitt"))
                                .filter("grpC", terms("accounts", "vladimir", "nigel"))
                                .build()
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "size": 0,
                          "aggregations" : {
                            "interactions" : {
                              "adjacency_matrix" : {
                                "filters" : {
                                  "grpA" : { "terms" : { "accounts" : ["hillary", "sidney"] }},
                                  "grpB" : { "terms" : { "accounts" : ["donald", "mitt"] }},
                                  "grpC" : { "terms" : { "accounts" : ["vladimir", "nigel"] }}
                                }
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void categorizeTextAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("categories",
                        categorizeText("message")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "categories": {
                              "categorize_text": {
                                "field": "message"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void categorizeTextAggregationWithSimilarityThreshold() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("categories",
                        categorizeText("message")
                                .withCategorizationFilters("\\w+\\_\\d{3}")
                                .withSimilarityThreshold(11)
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "categories": {
                              "categorize_text": {
                                "field": "message",
                                "categorization_filters": ["\\\\w+\\\\_\\\\d{3}"],
                                "similarity_threshold": 11
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void autoDateHistogramAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sales_over_time", autoDateHistogram("date", 10))
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                           "aggregations": {
                             "sales_over_time": {
                               "auto_date_histogram": {
                                 "field": "date",
                                 "buckets": 10
                               }
                             }
                           }
                         }
                        """
        );
    }

    @Test
    void autoDateHistogramAggregationWithFormat() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sales_over_time",
                        autoDateHistogram("date", 10)
                                .withFormat("yyyy-MM-dd")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                           "aggregations": {
                             "sales_over_time": {
                               "auto_date_histogram": {
                                 "field": "date",
                                 "buckets": 10,
                                 "format": "yyyy-MM-dd"
                               }
                             }
                           }
                         }
                        """
        );
    }

    @Test
    void autoDateHistogramAggregationWithMinimumInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sale_date",
                        autoDateHistogram("date", 10)
                                .withMinimumInterval(minute)
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "sale_date": {
                              "auto_date_histogram": {
                                "field": "date",
                                "buckets": 10,
                                "minimum_interval": "minute"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void autoDateHistogramAggregationWithMissing() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sale_date",
                        autoDateHistogram("date", 10)
                                .withMissing("2000/01/01")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "sale_date": {
                              "auto_date_histogram": {
                                "field": "date",
                                "buckets": 10,
                                "missing": "2000/01/01"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateHistogramAggregationWithCalendarInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sales_over_time",
                        dateHistogram("date", CalendarUnit.month)
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "sales_over_time": {
                              "date_histogram": {
                                "field": "date",
                                "calendar_interval": "month"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateHistogramAggregationWithFormat() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sales_over_time",
                        dateHistogram("date", CalendarUnit.month)
                                .withFormat("yyyy-MM-dd")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "sales_over_time": {
                              "date_histogram": {
                                "field": "date",
                                "calendar_interval": "month",
                                "format": "yyyy-MM-dd"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateHistogramAggregationWithTimezone() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("by_day",
                        dateHistogram("date", CalendarUnit.day)
                                .withTimeZone(ZoneOffset.ofHours(-1))
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "by_day": {
                              "date_histogram": {
                                "field":     "date",
                                "calendar_interval":  "day",
                                "time_zone": "-01:00"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateHistogramAggregationWithFixedInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("sales_over_time",
                        dateHistogram("date", 30, TimeUnit.days)
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "sales_over_time": {
                              "date_histogram": {
                                "field": "date",
                                "fixed_interval": "30d"
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateRangeAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("range",
                        dateRange("date",
                                until("2016/02/01"),
                                between("2016/02/01", "now/d"),
                                since("now/d")
                        ).withTimeZone(ZoneId.of("CET"))
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                           "aggregations": {
                               "range": {
                                   "date_range": {
                                       "field": "date",
                                       "ranges": [
                                          { "to": "2016/02/01" },
                                          { "from": "2016/02/01", "to" : "now/d" },
                                          { "from": "now/d" }
                                      ],
                                      "time_zone": "CET"
                                  }
                              }
                           }
                        }
                        """
        );
    }

    @Test
    void dateRangeAggregationWithKeys() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("range",
                        dateRange("date",
                                until("now-10M/M"),
                                since("now-10M/M")
                        )
                        .withFormat("MM-yyy")
                        .withKeys()
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "range": {
                              "date_range": {
                                "field": "date",
                                "ranges": [
                                  { "to": "now-10M/M" },
                                  { "from": "now-10M/M" }
                                ],
                                "format": "MM-yyy",
                                "keyed": true
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void dateRangeAggregationWithSpecifiedKeys() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("range",
                        dateRange("date",
                                between("01-2015", "03-2015").withKey("quarter_01"),
                                between("03-2015", "06-2015").withKey("quarter_02")
                        )
                        .withFormat("MM-yyy")
                        .withKeys()
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "range": {
                              "date_range": {
                                "field": "date",
                                "ranges": [
                                  { "from": "01-2015", "to": "03-2015", "key": "quarter_01" },
                                  { "from": "03-2015", "to": "06-2015", "key": "quarter_02" }
                                ],
                                "format": "MM-yyy",
                                "keyed": true
                              }
                            }
                          }
                        }
                        """
        );
    }


    @Test
    void diversifiedSamplerAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .withQuery(match("tags", "elasticsearch"))
                .aggregation("my_unbiased_sample",
                    diversifiedSampler("author", 200)
                        .aggregation("keywords", significantTerms("tags").withExclude("elasticsearch"))
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "query": {
                            "match": {
                              "tags": "elasticsearch"
                            }
                          },
                          "aggregations": {
                            "my_unbiased_sample": {
                              "aggregations": {
                                "keywords": {
                                  "significant_terms": {
                                    "field": "tags",
                                    "exclude": [ "elasticsearch" ]
                                  }
                                }
                              },
                              "diversified_sampler": {
                                "field": "author",
                                "shard_size": 200
                              }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void significantTermsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .withQuery(
                        terms("force", "British Transport Police")
                )
                .aggregation("significant_crime_types",
                        significantTerms("crime_type")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "query": {
                            "terms": { "force": [ "British Transport Police" ] }
                          },
                          "aggregations": {
                            "significant_crime_types": {
                              "significant_terms": { "field": "crime_type" }
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void termsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
                .aggregation("genres",
                        termsAgg("genre")
                )
                .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "aggregations": {
                            "genres": {
                              "terms": { "field": "genre" }
                            }
                          }
                        }
                        """
        );
    }
}
