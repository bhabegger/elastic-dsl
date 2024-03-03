package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticMatchPhraseClause.matchPhrase;

class ElasticSearchFullTextQueryTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void matchQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                match("firstname", "benjamin")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "match": {
                                    "firstname": "benjamin"
                                }
                            }
                        }
                        """
        );
    }
    @Test
    void matchPhraseQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            matchPhrase("title", "this is a test")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "match_phrase": {
                        "title": "this is a test"
                    }
                }
            }
            """
        );
    }

}