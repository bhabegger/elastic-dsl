package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAdjacencyMatrixAggregation.newAdjacencyMatrix;
import static tech.habegger.elastic.aggregation.ElasticAutoDateHistogramAggregation.autoDateHistogram;
import static tech.habegger.elastic.aggregation.ElasticCategorizeTextAggregation.categorizeText;
import static tech.habegger.elastic.aggregation.ElasticSignificantTermsAggregation.significantTerms;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;
import static tech.habegger.elastic.shared.TimeUnit.minute;

public class ElasticBucketAggregationsTest {
    @Test
    void adjacencyMatrixAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("interactions",
                newAdjacencyMatrix()
                    .filter("grpA",terms("accounts", "hillary", "sidney"))
                    .filter("grpB",terms("accounts", "donald", "mitt"))
                    .filter("grpC",terms("accounts", "vladimir", "nigel"))
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
