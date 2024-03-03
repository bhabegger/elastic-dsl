package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.response.ElasticSearchResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ElasticSearchResponseTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void simpleResponseUnmarshalling() throws JsonProcessingException {
        // Given
        var rawResponse = """
            {
              "took": 12,
              "timed_out": false,
              "_shards": {
                "total": 1,
                "successful": 1,
                "skipped": 0,
                "failed": 0
              },
              "hits": {
                "total": {
                  "value": 10000,
                  "relation": "gte"
                },
                "max_score": 1,
                "hits": [
                  {
                    "_index": "person",
                    "_id": "0011edf15f7f41d2aa9e8873ff754bb7_1",
                    "_score": 1,
                    "_source": {
                      "firstname": "Benjamin",
                      "birthdate": "1977-08-04",
                      "city": "Biel"
                    }
                  }
                ]
              }
            }
        """;

        // When
        ElasticSearchResponse<Person> actual = mapper.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual.getHits()).hasSize(1);
        assertThat(actual.getHits()).element(0).satisfies(hit -> {
            assertThat(hit.getId()).isEqualTo("0011edf15f7f41d2aa9e8873ff754bb7_1");
            assertThat(hit.getScore()).isEqualTo(1.0);
            assertThat(hit.getIndex()).isEqualTo("person");
            assertThat(hit.getSource()).isEqualTo(new Person("Benjamin", "1977-08-04", "Biel"));
        });
    }

    private record Person(String firstname, String birthdate, String city) {
    }
}
