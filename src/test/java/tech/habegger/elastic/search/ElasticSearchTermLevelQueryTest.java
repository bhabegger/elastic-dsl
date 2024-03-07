package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticExistsClause.exists;
import static tech.habegger.elastic.search.ElasticFuzzyClause.fuzzy;
import static tech.habegger.elastic.search.ElasticIdsClause.ids;
import static tech.habegger.elastic.search.ElasticPrefixClause.prefix;
import static tech.habegger.elastic.search.ElasticRangeClause.range;
import static tech.habegger.elastic.search.ElasticRegexpClause.RegexpFlags.ALL;
import static tech.habegger.elastic.search.ElasticRegexpClause.RegexpFlags.COMPLEMENT;
import static tech.habegger.elastic.search.ElasticRegexpClause.RegexpFlags.INTERSECTION;
import static tech.habegger.elastic.search.ElasticRegexpClause.regexp;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;
import static tech.habegger.elastic.search.RewriteMethod.*;

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
        var query = ElasticSearchRequest.query(
            range("birthdate", LocalDate.parse("2000-01-01"), null)
        );

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


    @Test
    void fuzzySimpleQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            fuzzy("user.id", "ki")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "fuzzy": {
                  "user.id": {
                    "value": "ki"
                  }
                }
              }
            }
            """
        );
    }

    @Test
    void fuzzyComplexQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            fuzzy("user.id", "ki")
                .withFuzziness(3,5)
                .withMaxExpansions(50)
                .withPrefixLength(0)
                .withoutTranspositions()
                .withRewrite(constant_score_blended)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "fuzzy": {
                  "user.id": {
                    "value": "ki",
                    "fuzziness": "AUTO:3,5",
                    "max_expansions": 50,
                    "prefix_length": 0,
                    "transpositions": false,
                    "rewrite": "constant_score_blended"
                  }
                }
              }
            }
            """
        );
    }


    @Test
    void idsQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            ids("1", "4", "100")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "ids" : {
                        "values" : ["1", "4", "100"]
                    }
                }
            }
            """
        );
    }

    @Test
    void regexpQuerySimple() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            regexp("user.id", "k.*y")
                .withFlags(ALL)
                .withoutCaseSensitivity()
                .withMaxDeterminizedStates(10000)
                .withRewrite(constant_score_blended)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "regexp": {
                        "user.id": {
                            "value": "k.*y",
                            "flags": "ALL",
                            "case_insensitive": true,
                            "max_determinized_states": 10000,
                            "rewrite": "constant_score_blended"
                        }
                    }
                }
            }
            """
        );
    }

    @Test
    void regexpQueryMultipleFlags() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                regexp("user.id", "k.*y")
                    .withFlags(COMPLEMENT, INTERSECTION)
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "regexp": {
                        "user.id": {
                            "value": "k.*y",
                            "flags": "COMPLEMENT|INTERSECTION"
                        }
                    }
                }
            }
            """
        );
    }
}