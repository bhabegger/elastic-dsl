package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticNestedClause.ScoreMode;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticNestedClause.nested;
import static tech.habegger.elastic.search.ElasticSearchRequest.query;
import static tech.habegger.elastic.search.ElasticTermClause.term;

class ElasticSearchJoinQueryTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    void nestedWithoutScoreMode() throws JsonProcessingException {
        // Given
        var query = query(
                nested("children", term("children.lastname", "habegger"))
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "nested": {
                                    "path": "children",
                                    "query": {
                                        "term": {
                                            "children.lastname": "habegger"
                                        }
                                    }
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void nestedWithScoreMode() throws JsonProcessingException {
        // Given
        var query = query(
                nested("children", term("children.lastname", "habegger"), ScoreMode.sum)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "nested": {
                                    "path": "children",
                                    "query": {
                                        "term": {
                                            "children.lastname": "habegger"
                                        }
                                    },
                                    "score_mode": "sum"
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void nestedIgnoringUnmapped() throws JsonProcessingException {
        // Given
        var query = query(
                nested("children", term("children.lastname", "habegger"), true)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                    {
                        "query": {
                            "nested": {
                                "path": "children",
                                "query": {
                                    "term": {
                                        "children.lastname": "habegger"
                                    }
                                },
                                "ignore_unmapped": true
                            }
                        }
                    }
                    """
        );
    }

}