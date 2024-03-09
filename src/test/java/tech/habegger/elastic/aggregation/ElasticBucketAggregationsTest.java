package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAutoDateHistogramAggregation.autoDateHistogram;
import static tech.habegger.elastic.shared.TimeUnit.minute;

public class ElasticBucketAggregationsTest {
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
}
