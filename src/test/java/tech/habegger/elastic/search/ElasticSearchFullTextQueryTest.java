package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticCombinedFieldsClause.Operator.and;
import static tech.habegger.elastic.search.ElasticCombinedFieldsClause.combinedFields;
import static tech.habegger.elastic.search.ElasticMatchBoolPrefixClause.matchBoolPrefix;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticMatchPhraseClause.matchPhrase;
import static tech.habegger.elastic.search.ElasticMatchPhrasePrefixClause.matchPhrasePrefix;
import static tech.habegger.elastic.search.ElasticMultiMatchClause.MultiMatchType.most_fields;
import static tech.habegger.elastic.search.ElasticMultiMatchClause.multiMatch;
import static tech.habegger.elastic.search.ElasticSearchRequest.query;

class ElasticSearchFullTextQueryTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void matchQuery() throws JsonProcessingException {
        // Given
        var query = query(
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
        var query = query(
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

    @Test
    void matchBoolPrefixQuery() throws JsonProcessingException {
        // Given
        var query = query(
            matchBoolPrefix("title", "this is a t")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "match_bool_prefix": {
                        "title": "this is a t"
                    }
                }
            }
            """
        );
    }

    @Test
    void matchPhrasePrefixQuery() throws JsonProcessingException {
        // Given
        var query = query(
            matchPhrasePrefix("title", "this is a t")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "match_phrase_prefix": {
                        "title": "this is a t"
                    }
                }
            }
            """
        );
    }

    @Test
    void multiMatchQuery() throws JsonProcessingException {
        // Given
        var query = query(
            multiMatch("this is a test", most_fields, "title", "description")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "multi_match": {
                        "query": "this is a test",
                        "fields": ["title", "description"],
                        "type": "most_fields"
                    }
                }
            }
            """
        );
    }


    @Test
    void combinedFieldsQuery() throws JsonProcessingException {
        // Given
        var query = query(
            combinedFields(
                "database systems",
                and,
                "title", "abstract"
            )
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "combined_fields" : {
                  "query":      "database systems",
                  "fields":     [ "title", "abstract"],
                  "operator":   "and"
                }
              }
            }
            """
        );
    }

    @Test
    void combinedFieldsQueryWithAndOperator() throws JsonProcessingException {
        // Given
        var query = query(
                combinedFields(
                        "database systems",
                        "title", "abstract"
                ).withAndOperator()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                          "query": {
                            "combined_fields" : {
                              "query":      "database systems",
                              "fields":     [ "title", "abstract"],
                              "operator":   "and"
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void combinedFieldsQueryWithMinimumShouldMatch() throws JsonProcessingException {
        // Given
        var query = query(
                combinedFields(
                        "database systems",
                        "title", "abstract"
                ).withMinimumShouldMatch(2)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "combined_fields" : {
                  "query":      "database systems",
                  "fields":     [ "title", "abstract"],
                  "minimum_should_match":   2
                }
              }
            }
            """
        );
    }
}