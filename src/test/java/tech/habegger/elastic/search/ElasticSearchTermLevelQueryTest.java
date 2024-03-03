package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticExistsClause.exists;
import static tech.habegger.elastic.search.ElasticPrefixClause.prefix;
import static tech.habegger.elastic.search.ElasticRangeClause.range;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;

class ElasticSearchTermLevelQueryTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    void termQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            term("firstname", "benjamin")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "term": {
                                    "firstname": "benjamin"
                                }
                            }
                        }
                        """
        );
    }
    @Test
    void termsQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            terms("firstname", "benjamin", "rocio")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "terms": {
                                    "firstname": [ "benjamin", "rocio" ]
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void prefixQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            prefix("firstname", "ben")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "prefix": {
                                    "firstname": "ben"
                                }
                            }
                        }
                        """
        );
    }
    @Test
    void existsQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                exists("location")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "exists": {
                                    "field": "location"
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void rangeQueryGteOnly() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
            range("birthdate", LocalDate.parse("2000-01-01"), null)
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "range": {
                        "birthdate": {
                            "gte": "2000-01-01"
                        }
                    }
                }
            }
            """
        );
    }

    @Test
    void rangeQueryLteOnly() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                range("birthdate", null, LocalDate.parse("2000-01-01"))
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "range": {
                                    "birthdate": {
                                        "lte": "2000-01-01"
                                    }
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void rangeQueryBoth() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01"))
        ).build();

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
                """
                        {
                            "query": {
                                "range": {
                                    "birthdate": {
                                        "gte": "1990-01-01",
                                        "lte": "2000-01-01"
                                    }
                                }
                            }
                        }
                        """
        );
    }

    @Test
    void booleanWithMust() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                ElasticBooleanClause.newBool()
                        .must(term("firstname", "benjamin"))
                        .must(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
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
                                    "must": [
                                        {
                                            "term": {
                                                "firstname": "benjamin"
                                            }
                                        },
                                        {
                                            "range": {
                                                "birthdate": {
                                                    "gte": "1990-01-01",
                                                    "lte": "2000-01-01"
                                                }
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
        var query = ElasticSearchRequest.requestBuilder().withQuery(
                ElasticBooleanClause.newBool()
                    .must(term("firstname", "benjamin"))
                    .should(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
                    .filter(term("city", "biel"))
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