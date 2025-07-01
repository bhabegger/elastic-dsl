package tech.habegger.elastic.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;

public class ElasticIndexUpsertResponseTest {

    @Test
    void unmarshalAcknowledgedCOrrectly() throws JsonProcessingException {
        // Given
        var rawResponse = """
            {
              "acknowledged": true
            }
            """;

        // When
        ElasticIndexUpsertResponse actual = MAPPER.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual.acknowledged()).isTrue();
    }

    @Test
    void unmarshalErrorCorrectly() throws JsonProcessingException {
        // Given
        var rawResponse = """
            {
              "error": {
                "root_cause": [
                  {
                    "type": "mapper_parsing_exception",
                    "reason": "No type specified for field [document]"
                  }
                ],
                "type": "mapper_parsing_exception",
                "reason": "Failed to parse mapping: No type specified for field [document]",
                "caused_by": {
                  "type": "mapper_parsing_exception",
                  "reason": "No type specified for field [document]"
                }
              },
              "status": 400
            }
        """;

        // When
        ElasticIndexUpsertResponse actual = MAPPER.readValue(rawResponse, new TypeReference<>() {});

        // Then
        assertThat(actual.error()).isNotNull();
        assertThat(actual.error().rootCause()).hasSize(1);
        assertThat(actual.status()).isEqualTo(400);
    }
}
