package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticBooleanClause.newBool;
import static tech.habegger.elastic.search.ElasticDistanceFeatureClause.distanceFeature;
import static tech.habegger.elastic.search.ElasticKnn.knn;
import static tech.habegger.elastic.search.ElasticMatchAllClause.matchAll;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticMoreLikeThisClause.newMoreLikeThis;
import static tech.habegger.elastic.search.ElasticPercolateClause.percolate;
import static tech.habegger.elastic.search.ElasticRankFeatureClause.rankFeature;
import static tech.habegger.elastic.search.ElasticScriptScoreClause.scriptScore;
import static tech.habegger.elastic.search.GeoCoord.geoCoord;
import static tech.habegger.elastic.search.ScriptExpression.scriptInline;

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

    @Test
    void distanceFeatureTemporalQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                newBool()
                    .must(match("name", "chocolate"))
                    .should(distanceFeature("production_date", "7d", "now"))
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
                              "should": [{
                                "distance_feature": {
                                  "field": "production_date",
                                  "pivot": "7d",
                                  "origin": "now"
                                }
                              }],
                              "must": [{
                                "match": {
                                  "name": "chocolate"
                                }
                              }]
                            }
                          }
                        }
                        """
        );
    }

    @Test
    void distanceFeatureDistanceQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                newBool()
                    .must(match("name", "chocolate"))
                    .should(distanceFeature("location", "1000m", geoCoord(-71.3f, 41.15f)))
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
                  "should": [{
                    "distance_feature": {
                      "field": "location",
                      "pivot": "1000m",
                      "origin": [-71.3, 41.15]
                    }
                  }],
                  "must": [{
                    "match": {
                      "name": "chocolate"
                    }
                  }]
                }
              }
            }
            """
        );
    }

    @Test
    void moreLikeThisQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            newMoreLikeThis()
                .fields("title", "description")
                .like("imdb", "1")
                .like("imdb", "2")
                .like("and potentially some more text here as well")
                .withMinTermFrequency(1)
                .withMaxQueryTerms(12)
            .build()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "more_like_this": {
                  "fields": [ "title", "description" ],
                  "like": [
                    {
                      "_index": "imdb",
                      "_id": "1"
                    },
                    {
                      "_index": "imdb",
                      "_id": "2"
                    },
                    "and potentially some more text here as well"
                  ],
                  "max_query_terms": 12,
                  "min_term_freq": 1
                }
              }
            }
            """
        );
    }
    @Test
    void moreLikeThisQueryWithInlineDoc() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                newMoreLikeThis()
                    .fields("name.first", "name.last")
                    .like("marvel",
                        new MyMarvelDoc(
                            new MyNameRecord("Ben", "Grimm"),
                        "You got no idea what I'd... what I'd give to be invisible."
                        )
                    )
                    .like("marvel", "2")
                    .withMinTermFrequency(1)
                    .withMaxQueryTerms(12)
                .build()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "more_like_this": {
                  "fields": [ "name.first", "name.last" ],
                  "like": [
                    {
                      "_index": "marvel",
                      "doc": {
                        "name": {
                          "first": "Ben",
                          "last": "Grimm"
                        },
                        "_doc": "You got no idea what I'd... what I'd give to be invisible."
                      }
                    },
                    {
                      "_index": "marvel",
                      "_id": "2"
                    }
                  ],
                  "max_query_terms": 12,
                  "min_term_freq": 1
                }
              }
            }
            """
        );
    }

    @Test
    void percolateQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            percolate(
                "query",
                Map.of("message", "A new bonsai tree in the office")
            )
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "percolate": {
                  "field": "query",
                  "document": {
                    "message": "A new bonsai tree in the office"
                  }
                }
              }
            }
            """
        );
    }

    @Test
    void rankFeatureQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            newBool()
                .must(match("content", "2016"))
                .should(rankFeature("pagerank"))
                .should(rankFeature("url_length",0.1f))
                .should(rankFeature("topics.sports", 0.4f))
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
                          "rank_feature": {
                            "field": "pagerank"
                          }
                        },
                        {
                          "rank_feature": {
                            "field": "url_length",
                            "boost": 0.1
                          }
                        },
                        {
                          "rank_feature": {
                            "field": "topics.sports",
                            "boost": 0.4
                          }
                        }
                      ],
                      "must": [
                        {
                          "match": {
                            "content": "2016"
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
    void scriptScoreQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            scriptScore(
                match("message", "elasticsearch"),
                scriptInline("doc['my-int'].value / 10 ")
            )
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "script_score": {
                  "query": {
                    "match": { "message": "elasticsearch" }
                  },
                  "script": {
                    "source": "doc['my-int'].value / 10 "
                  }
                }
              }
            }
            """
        );
    }

    record MyMarvelDoc(MyNameRecord name, String _doc) {}
    record MyNameRecord(String first, String last) {}
}