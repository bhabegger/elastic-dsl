package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticFunctionScoreClause.newFunctionScore;
import static tech.habegger.elastic.search.ElasticRangeClause.range;
import static tech.habegger.elastic.search.ElasticTermClause.term;

class ElasticSearchFunctionScoreQueryTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void functionScoreQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            newFunctionScore(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
                .function(term("firstname", "benjamin"), 1.5)
            .build()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "function_score": {
                  "query": {
                    "range": {
                      "birthdate": {
                        "gte": "1990-01-01",
                        "lte": "2000-01-01"
                      }
                    }
                  },
                  "functions": [
                    {
                      "filter": {
                        "term": {
                          "firstname": "benjamin"
                        }
                      },
                      "weight": 1.5
                    }
                  ]
                }
              }
            }
            """
        );
    }

    @Test
    void booleanWithMustAndShould() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                ElasticBooleanClause.newBool()
                        .must(term("firstname", "benjamin"))
                        .should(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
                        .build()
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "bool": {
                                    "should": [
                                        {
                                            "range": {
                                                "birthdate": {
                                                    "gte": "1990-01-01",
                                                    "lte": "2000-01-01"
                                                }
                                            }
                                        }
                                    ],
                                    "must": [
                                        {
                                            "term": {
                                                "firstname": "benjamin"
                                            }
                                        }
                                    ]
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void booleanWithMustShouldAndFilter() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                ElasticBooleanClause.newBool()
                    .must(term("firstname", "benjamin"))
                    .should(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
                    .filter(term("city", "biel"))
                    .build()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "bool": {
                                    "should": [
                                        {
                                            "range": {
                                                "birthdate": {
                                                    "gte": "1990-01-01",
                                                    "lte": "2000-01-01"
                                                }
                                            }
                                        }
                                    ],
                                    "must": [
                                        {
                                            "term": {
                                                "firstname": "benjamin"
                                            }
                                        }
                                    ],
                                    "filter": [
                                        {
                                            "term": {
                                                "city": "biel"
                                            }
                                        }
                                    ]
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void booleanWithShouldWithMinimum() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                ElasticBooleanClause.newBool()
                    .should(term("firstname", "benjamin"))
                    .should(term("firstname", "rocio"))
                    .minimumShouldMatch(1)
                .build()
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "bool": {
                                    "should": [
                                        {
                                            "term": {
                                                "firstname": "benjamin"
                                            }
                                        },
                                        {
                                            "term": {
                                                "firstname": "rocio"
                                            }
                                        }
                                    ],
                                    "minimum_should_match": 1
                                }
                            }
                        }
                        """
        );
    }

}