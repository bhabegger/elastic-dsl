package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticKnn.knn;
import static tech.habegger.elastic.search.ElasticMatchAllClause.matchAll;

class ElasticSearchSpecializedQueryTest {

    ObjectMapper mapper = new ObjectMapper();


    @Test
    void knnQuery() throws JsonProcessingException {
        // Given
        var vector = new float[]{1.0F,2.0F,3.0F};
        var query = ElasticSearchRequest.requestBuilder().withKnn(
            knn("face_vector",vector,3, 100, matchAll())
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "knn": {
                "field": "face_vector",
                "query_vector": [
                  1.0,
                  2.0,
                  3.0
                ],
                "k": 3,
                "num_candidates": 100,
                "filter": {
                  "match_all": {}
                }
              }
            }
            """
        );
    }

}