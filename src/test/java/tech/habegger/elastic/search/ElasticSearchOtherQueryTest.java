package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.search.ElasticMatchAllClause.matchAll;

class ElasticSearchOtherQueryTest {
    @Test
    void matchAllQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            matchAll()
        );

        // When
        var actual = MAPPER.writeValueAsString(query);

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