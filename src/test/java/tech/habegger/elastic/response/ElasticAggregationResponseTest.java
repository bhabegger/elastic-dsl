package tech.habegger.elastic.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;

public class ElasticAggregationResponseTest {
    @Test
    void filterAggregationResponse() throws JsonProcessingException {
        // Given
        var rawResponse = """
            {
              "aggregations": {
                "avg_price": { "value": 140.71428571428572 },
                "t_shirts": {
                  "doc_count": 3,
                  "avg_price": { "value": 128.33333333333334 }
                }
              }
            }
        """;

        // When
        ElasticSearchResponse<Object> actual = MAPPER.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual).isNotNull();

        var tShirtsAgg = actual.getAggregation("t_shirts", ElasticFilterAggregationResponse.class);
        assertThat(tShirtsAgg).isNotNull();
        assertThat(tShirtsAgg.docCount()).isEqualTo(3);

        var avgTShirtPriceAgg = tShirtsAgg.getAggregation("avg_price", ElasticMetricsAggregationResponse.class);
        assertThat(avgTShirtPriceAgg).isNotNull();
        assertThat(avgTShirtPriceAgg.doubleValue()).isCloseTo(128.33333333333334, Percentage.withPercentage(0.00001));

    }

    @Test
    void avgAggregationResponse() throws JsonProcessingException {
        // Given
        var rawResponse =
            """
                {
                  "aggregations": {
                    "avg_grade": {
                      "value": 75.0
                    }
                  }
                }
            """;

        // When
        ElasticSearchResponse<Object> actual = MAPPER.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual).isNotNull();

        var avgGradeAgg = actual.getAggregation("avg_grade", ElasticMetricsAggregationResponse.class);
        assertThat(avgGradeAgg).isNotNull();
        assertThat(avgGradeAgg.doubleValue()).isCloseTo(75, Percentage.withPercentage(0.00001));
    }

    @Test
    void dataRangeAggregationResponse() throws JsonProcessingException {
        // Given
        var rawResponse = """
            {
              "aggregations": {
                "range": {
                  "buckets": [
                    {
                      "to": 1.4436576E12,
                      "to_as_string": "10-2015",
                      "doc_count": 7,
                      "key": "*-10-2015"
                    },
                    {
                      "from": 1.4436576E12,
                      "from_as_string": "10-2015",
                      "doc_count": 0,
                      "key": "10-2015-*"
                    }
                  ]
                }
              }
            }
            """;

        // When
        ElasticSearchResponse<Object> actual = MAPPER.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual).isNotNull();

        var rangeAgg = actual.getAggregation("range", ElasticBucketsAggregationResponse.class);
        assertThat(rangeAgg).isNotNull();

        var buckets = rangeAgg.buckets();
        assertThat(buckets).hasSize(2);
    }
}
