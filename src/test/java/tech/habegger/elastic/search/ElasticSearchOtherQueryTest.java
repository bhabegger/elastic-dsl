package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticMatchAllClause.matchAll;

class ElasticSearchOtherQueryTest {

    ObjectMapper mapper = new ObjectMapper();


    @Test
    void matchAllQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            matchAll()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "match_all": {}
                            }
                        }
                        """
        );
    }

}