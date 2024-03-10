package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.search.ElasticSearchRequest;
import tech.habegger.elastic.shared.*;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticAdjacencyMatrixAggregation.newAdjacencyMatrix;
import static tech.habegger.elastic.aggregation.ElasticAutoDateHistogramAggregation.autoDateHistogram;
import static tech.habegger.elastic.aggregation.ElasticAvgAggregation.avg;
import static tech.habegger.elastic.aggregation.ElasticCategorizeTextAggregation.categorizeText;
import static tech.habegger.elastic.aggregation.ElasticDateHistogramAggregation.dateHistogram;
import static tech.habegger.elastic.aggregation.ElasticDateRangeAggregation.dateRange;
import static tech.habegger.elastic.aggregation.ElasticDiversifiedSamplerAggregation.diversifiedSampler;
import static tech.habegger.elastic.aggregation.ElasticFiltersAggregation.newFilters;
import static tech.habegger.elastic.aggregation.ElasticFrequentItemSetsAggregation.FieldSpec.field;
import static tech.habegger.elastic.aggregation.ElasticFrequentItemSetsAggregation.frequentItemSets;
import static tech.habegger.elastic.aggregation.ElasticGeoDistanceAggregation.geoDistance;
import static tech.habegger.elastic.aggregation.ElasticGeohashGridAggregation.geohashGrid;
import static tech.habegger.elastic.aggregation.ElasticGeohexGridAggregation.geohexGrid;
import static tech.habegger.elastic.aggregation.ElasticGeotileGridAggregation.geotileGrid;
import static tech.habegger.elastic.aggregation.ElasticGlobalAggregation.global;
import static tech.habegger.elastic.aggregation.ElasticHistogramAggregation.histogram;
import static tech.habegger.elastic.aggregation.ElasticIpPrefixAggregation.ipPrefix;
import static tech.habegger.elastic.aggregation.ElasticIpRangeAggregation.ipRange;
import static tech.habegger.elastic.aggregation.ElasticMinAggregation.min;
import static tech.habegger.elastic.aggregation.ElasticMissingAggregation.missing;
import static tech.habegger.elastic.aggregation.ElasticMultiTermsAggregation.multiTerms;
import static tech.habegger.elastic.aggregation.ElasticMultiTermsAggregation.termSpec;
import static tech.habegger.elastic.aggregation.ElasticNestedAggregation.nestedAgg;
import static tech.habegger.elastic.aggregation.ElasticRangeAggregation.rangeAgg;
import static tech.habegger.elastic.aggregation.ElasticRareTermsAggregation.rareTerms;
import static tech.habegger.elastic.aggregation.ElasticSignificantTermsAggregation.significantTerms;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
import static tech.habegger.elastic.search.ElasticConstantScoreClause.constantScore;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticRangeClause.range;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;
import static tech.habegger.elastic.shared.DateTimeUnit.minute;
import static tech.habegger.elastic.shared.DistanceType.plane;
import static tech.habegger.elastic.shared.DistanceUnit.kilometers;
import static tech.habegger.elastic.shared.GeoCoord.geoCoord;
import static tech.habegger.elastic.shared.GeoRect.geoRect;
import static tech.habegger.elastic.shared.IpRange.mask;

public class ElasticBucketAggregationsTest {
    @Test
    void adjacencyMatrixAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("interactions",
                newAdjacencyMatrix()
                    .filter("grpA", terms("accounts", "hillary", "sidney"))
                    .filter("grpB", terms("accounts", "donald", "mitt"))
                    .filter("grpC", terms("accounts", "vladimir", "nigel"))
                    .build()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations" : {
                    "interactions" : {
                      "adjacency_matrix" : {
                        "filters" : {
                          "grpA" : { "terms" : { "accounts" : ["hillary", "sidney"] }},
                          "grpB" : { "terms" : { "accounts" : ["donald", "mitt"] }},
                          "grpC" : { "terms" : { "accounts" : ["vladimir", "nigel"] }}
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void categorizeTextAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("categories",
                categorizeText("message")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "categories": {
                      "categorize_text": {
                        "field": "message"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void categorizeTextAggregationWithSimilarityThreshold() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("categories",
                categorizeText("message")
                    .withCategorizationFilters("\\w+\\_\\d{3}")
                    .withSimilarityThreshold(11)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "categories": {
                      "categorize_text": {
                        "field": "message",
                        "categorization_filters": ["\\\\w+\\\\_\\\\d{3}"],
                        "similarity_threshold": 11
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void autoDateHistogramAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sales_over_time", autoDateHistogram("date", 10))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                   "aggregations": {
                     "sales_over_time": {
                       "auto_date_histogram": {
                         "field": "date",
                         "buckets": 10
                       }
                     }
                   }
                 }
                """
        );
    }

    @Test
    void autoDateHistogramAggregationWithFormat() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sales_over_time",
                autoDateHistogram("date", 10)
                    .withFormat("yyyy-MM-dd")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                   "aggregations": {
                     "sales_over_time": {
                       "auto_date_histogram": {
                         "field": "date",
                         "buckets": 10,
                         "format": "yyyy-MM-dd"
                       }
                     }
                   }
                 }
                """
        );
    }

    @Test
    void autoDateHistogramAggregationWithMinimumInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sale_date",
                autoDateHistogram("date", 10)
                    .withMinimumInterval(minute)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "sale_date": {
                      "auto_date_histogram": {
                        "field": "date",
                        "buckets": 10,
                        "minimum_interval": "minute"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void autoDateHistogramAggregationWithMissing() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sale_date",
                autoDateHistogram("date", 10)
                    .withMissing("2000/01/01")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "sale_date": {
                      "auto_date_histogram": {
                        "field": "date",
                        "buckets": 10,
                        "missing": "2000/01/01"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateHistogramAggregationWithCalendarInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sales_over_time",
                dateHistogram("date", CalendarUnit.month)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "sales_over_time": {
                      "date_histogram": {
                        "field": "date",
                        "calendar_interval": "month"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateHistogramAggregationWithFormat() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sales_over_time",
                dateHistogram("date", CalendarUnit.month)
                    .withFormat("yyyy-MM-dd")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "sales_over_time": {
                      "date_histogram": {
                        "field": "date",
                        "calendar_interval": "month",
                        "format": "yyyy-MM-dd"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateHistogramAggregationWithTimezone() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("by_day",
                dateHistogram("date", CalendarUnit.day)
                    .withTimeZone(ZoneOffset.ofHours(-1))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "by_day": {
                      "date_histogram": {
                        "field":     "date",
                        "calendar_interval":  "day",
                        "time_zone": "-01:00"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateHistogramAggregationWithFixedInterval() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("sales_over_time",
                dateHistogram("date", 30, TimeUnit.days)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "sales_over_time": {
                      "date_histogram": {
                        "field": "date",
                        "fixed_interval": "30d"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateRangeAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("range",
                dateRange("date",
                    DateRange.until("2016/02/01"),
                    DateRange.between("2016/02/01", "now/d"),
                    DateRange.since("now/d")
                ).withTimeZone(ZoneId.of("CET"))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                   "aggregations": {
                       "range": {
                           "date_range": {
                               "field": "date",
                               "ranges": [
                                  { "to": "2016/02/01" },
                                  { "from": "2016/02/01", "to" : "now/d" },
                                  { "from": "now/d" }
                              ],
                              "time_zone": "CET"
                          }
                      }
                   }
                }
                """
        );
    }

    @Test
    void dateRangeAggregationWithKeys() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("range",
                dateRange("date",
                    DateRange.until("now-10M/M"),
                    DateRange.since("now-10M/M")
                )
                    .withFormat("MM-yyy")
                    .withKeyed()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "range": {
                      "date_range": {
                        "field": "date",
                        "ranges": [
                          { "to": "now-10M/M" },
                          { "from": "now-10M/M" }
                        ],
                        "format": "MM-yyy",
                        "keyed": true
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void dateRangeAggregationWithSpecifiedKeys() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("range",
                dateRange("date",
                    DateRange.between("01-2015", "03-2015").withKey("quarter_01"),
                    DateRange.between("03-2015", "06-2015").withKey("quarter_02")
                )
                    .withFormat("MM-yyy")
                    .withKeyed()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "range": {
                      "date_range": {
                        "field": "date",
                        "ranges": [
                          { "from": "01-2015", "to": "03-2015", "key": "quarter_01" },
                          { "from": "03-2015", "to": "06-2015", "key": "quarter_02" }
                        ],
                        "format": "MM-yyy",
                        "keyed": true
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void diversifiedSamplerAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(match("tags", "elasticsearch"))
            .aggregation("my_unbiased_sample",
                diversifiedSampler("author", 200)
                    .aggregation("keywords", significantTerms("tags").withExclude("elasticsearch"))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "match": {
                      "tags": "elasticsearch"
                    }
                  },
                  "aggregations": {
                    "my_unbiased_sample": {
                      "aggregations": {
                        "keywords": {
                          "significant_terms": {
                            "field": "tags",
                            "exclude": [ "elasticsearch" ]
                          }
                        }
                      },
                      "diversified_sampler": {
                        "field": "author",
                        "shard_size": 200
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void filtersAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("messages",
                newFilters()
                    .filter("errors", match("body", "error"))
                    .filter("warnings", match("body", "warning"))
                    .build()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations" : {
                    "messages" : {
                      "filters" : {
                        "filters" : {
                          "errors" :   { "match" : { "body" : "error"   }},
                          "warnings" : { "match" : { "body" : "warning" }}
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void frequentItemSetAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("items",
                frequentItemSets("my_field_1", "my_field_2")
                    .withMinimumSetSize(3)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                    "aggregations" : {
                        "items" : {
                            "frequent_item_sets": {
                                "fields": [
                                    {"field": "my_field_1"},
                                    {"field": "my_field_2"}
                                ],
                                "minimum_set_size": 3
                            }
                        }
                    }
                }
                """
        );
    }

    @Test
    void frequentItemSetAggregationWithExclusions() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("my_agg",
                frequentItemSets(
                    field("category.keyword"),
                    field("geoip.city_name").exclude("other")
                )
                    .withMinimumSetSize(3)
                    .withSize(3)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                   "size":0,
                   "aggregations":{
                      "my_agg":{
                         "frequent_item_sets":{
                            "fields":[
                               {
                                  "field":"category.keyword"
                               },
                               {
                                  "field":"geoip.city_name",
                                  "exclude": [ "other" ]
                               }
                            ],
                            "minimum_set_size":3,
                            "size":3
                         }
                      }
                   }
                }
                """
        );
    }


    @Test
    void frequentItemSetAggregationWithFilter() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("my_agg",
                frequentItemSets(
                    field("category.keyword"),
                    field("geoip.city_name")
                )
                    .withMinimumSetSize(3)
                    .withSize(3)
                    .withFilter(term("geoip.continent_name", "Europe"))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "my_agg": {
                      "frequent_item_sets": {
                        "fields": [
                          { "field": "category.keyword" },
                          { "field": "geoip.city_name" }
                        ],
                        "minimum_set_size": 3,
                        "size": 3,
                        "filter": {
                          "term": {
                            "geoip.continent_name": "Europe"
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoDistanceAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("rings",
                geoDistance("location", geoCoord(4.894f, 52.3760f),
                    Range.to(100f),
                    Range.between(100f, 300f),
                    Range.from(300f)
                )
                    .withUnit(kilometers)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "rings": {
                      "geo_distance": {
                        "field": "location",
                        "origin": {
                          "lat" : 4.894,
                          "lon" : 52.376
                        },
                        "ranges": [
                          { "to": 100.0 },
                          { "from": 100.0, "to": 300.0 },
                          { "from": 300.0 }
                        ],
                        "unit": "km"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoDistanceAggregationWithDistanceType() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("rings",
                geoDistance("location", geoCoord(4.894f, 52.3760f),
                    Range.to(100f),
                    Range.between(100f, 300f),
                    Range.from(300f)
                )
                    .withUnit(kilometers)
                    .withDistanceType(plane)

            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "rings": {
                      "geo_distance": {
                        "field": "location",
                        "origin": {
                          "lat" : 4.894,
                          "lon" : 52.376
                        },
                        "ranges": [
                          { "to": 100.0 },
                          { "from": 100.0, "to": 300.0 },
                          { "from": 300.0 }
                        ],
                        "distance_type": "plane",
                        "unit": "km"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoDistanceAggregationWithKeyed() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("rings_around_amsterdam",
                geoDistance("location", geoCoord(4.894f, 52.3760f),
                    Range.to(100000f).withKey("first_ring"),
                    Range.between(100000f, 300000f).withKey("second_ring"),
                    Range.from(300000f).withKey("third_ring")
                )
                    .withKeyed()

            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "rings_around_amsterdam": {
                      "geo_distance": {
                        "field": "location",
                        "origin": {
                          "lat" : 4.894,
                          "lon" : 52.376
                        },
                        "ranges": [
                          { "to": 100000.0, "key": "first_ring" },
                          { "from": 100000.0, "to": 300000.0, "key": "second_ring" },
                          { "from": 300000.0, "key": "third_ring" }
                        ],
                        "keyed": true
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoHashGridAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("large-grid",
                geohashGrid("location", 3)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "large-grid": {
                      "geohash_grid": {
                        "field": "location",
                        "precision": 3
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoHashGridAggregationWithBoundingBox() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("tiles-in-bounds",
                geohashGrid("location", 8)
                    .withBounds(geoRect(
                        4.21875f, 53.4375f,
                        5.625f, 52.03125f
                    ))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "tiles-in-bounds": {
                      "geohash_grid": {
                        "field": "location",
                        "precision": 8,
                        "bounds": {
                          "top_left": {
                              "lat" : 4.21875,
                              "lon" : 53.4375
                          },
                          "bottom_right": {
                              "lat" : 5.625,
                              "lon" : 52.03125
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoHexGridAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("large-grid",
                geohexGrid("location", 3)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "large-grid": {
                      "geohex_grid": {
                        "field": "location",
                        "precision": 3
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoHexGridAggregationWithBoundingBox() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("tiles-in-bounds",
                geohexGrid("location", 8)
                    .withBounds(geoRect(
                        4.21875f, 53.4375f,
                        5.625f, 52.03125f
                    ))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "tiles-in-bounds": {
                      "geohex_grid": {
                        "field": "location",
                        "precision": 8,
                        "bounds": {
                          "top_left": {
                              "lat" : 4.21875,
                              "lon" : 53.4375
                          },
                          "bottom_right": {
                              "lat" : 5.625,
                              "lon" : 52.03125
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void geoTileGridAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("large-grid",
                geotileGrid("location", 3)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "large-grid": {
                      "geotile_grid": {
                        "field": "location",
                        "precision": 3
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void geoTileGridAggregationWithBoundingBox() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("tiles-in-bounds",
                geotileGrid("location", 8)
                    .withBounds(geoRect(
                        4.21875f, 53.4375f,
                        5.625f, 52.03125f
                    ))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "tiles-in-bounds": {
                      "geotile_grid": {
                        "field": "location",
                        "precision": 8,
                        "bounds": {
                          "top_left": {
                              "lat" : 4.21875,
                              "lon" : 53.4375
                          },
                          "bottom_right": {
                              "lat" : 5.625,
                              "lon" : 52.03125
                          }
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void globalAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(match("type", "t-shirt"))
            .aggregation("all_products",
                global()
                    .aggregation("avg_price", avg("price"))
            )
            .aggregation("t_shirts", avg("price"))
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "match": { "type": "t-shirt" }
                  },
                  "aggregations": {
                    "all_products": {
                      "aggregations": {
                        "avg_price": { "avg": { "field": "price" } }
                      },
                      "global": {}
                    },
                    "t_shirts": { "avg": { "field": "price" } }
                  }
                }
                """
        );
    }

    @Test
    void histogramAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("prices",
                histogram("price", 50.0f)
            )
        .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "prices": {
                      "histogram": {
                        "field": "price",
                        "interval": 50.0
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void histogramAggregationWithExtendedBounds() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(constantScore(range("price", null, 500)))
            .aggregation("prices",
                histogram("price", 50f)
                    .withExtendedBounds(0,500)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "constant_score": { "filter": { "range": { "price": { "lte": "500" } } } }
                  },
                  "aggregations": {
                    "prices": {
                      "histogram": {
                        "field": "price",
                        "interval": 50.0,
                        "extended_bounds": {
                          "min": 0.0,
                          "max": 500.0
                        }
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void histogramAggregationWithHardBounds() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(constantScore(range("price", null, 500)))
            .aggregation("prices",
                histogram("price", 50f)
                    .withHardBounds(100,200)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "constant_score": { "filter": { "range": { "price": { "lte": "500" } } } }
                  },
                  "aggregations": {
                    "prices": {
                      "histogram": {
                        "field": "price",
                        "interval": 50.0,
                        "hard_bounds": {
                          "min": 100.0,
                          "max": 200.0
                        }
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void histogramAggregationWitMissing() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("quantity",
                histogram("quantity", 10f)
                    .withMissing(0f)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "quantity": {
                      "histogram": {
                        "field": "quantity",
                        "interval": 10.0,
                        "missing": 0.0
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void ipPrefixAggregationWithIpV6() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("ipv6-subnets",
                ipPrefix("ipv6", 64)
                    .withIpV6()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "ipv6-subnets": {
                      "ip_prefix": {
                        "field": "ipv6",
                        "prefix_length": 64,
                        "is_ipv6": true
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void ipPrefixAggregationWithAppendPrefixLength() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("ipv4-subnets",
                ipPrefix("ipv4", 24)
                    .withAppendPrefixLength()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "ipv4-subnets": {
                      "ip_prefix": {
                        "field": "ipv4",
                        "prefix_length": 24,
                        "append_prefix_length": true
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void ipPrefixAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("ipv4-subnets",
                ipPrefix("ipv4", 24)
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "ipv4-subnets": {
                      "ip_prefix": {
                        "field": "ipv4",
                        "prefix_length": 24
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void ipRangeAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(10)
            .aggregation("ip_ranges",
                ipRange("ip",
                    IpRange.to("10.0.0.5"),
                    IpRange.from("10.0.0.5")
                )
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 10,
                  "aggregations": {
                    "ip_ranges": {
                      "ip_range": {
                        "field": "ip",
                        "ranges": [
                          { "to": "10.0.0.5" },
                          { "from": "10.0.0.5" }
                        ]
                      }
                    }
                  }
                }
                """
        );
    }
    @Test
    void ipRangeAggregationWithMasks() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("ip_ranges",
                ipRange("ip",
                    mask("10.0.0.0/25"),
                    mask("10.0.0.127/25")
                )
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "ip_ranges": {
                      "ip_range": {
                        "field": "ip",
                        "ranges": [
                          { "mask": "10.0.0.0/25" },
                          { "mask": "10.0.0.127/25" }
                        ]
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void ipRangeAggregationWithKeyed() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("ip_ranges",
                ipRange("ip",
                    IpRange.to("10.0.0.5"),
                    IpRange.from("10.0.0.5")
                ).withKeyed()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "size": 0,
                  "aggregations": {
                    "ip_ranges": {
                      "ip_range": {
                        "field": "ip",
                        "ranges": [
                          { "to": "10.0.0.5" },
                          { "from": "10.0.0.5" }
                        ],
                        "keyed": true
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void missingAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withSize(0)
            .aggregation("products_without_a_price",
                missing("price")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                    "size": 0,
                    "aggregations": {
                        "products_without_a_price": {
                            "missing": { "field": "price" }
                        }
                    }
                }
                """
        );
    }



    @Test
    void multiTermsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("genres_and_products",
                multiTerms("genre", "product")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "genres_and_products": {
                      "multi_terms": {
                        "terms": [{
                          "field": "genre"
                        }, {
                          "field": "product"
                        }]
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void multiTermsAggregationWithMissing() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("genres_and_products",
                multiTerms(termSpec("genre"), termSpec("product").withMissing("Product Z"))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "genres_and_products": {
                      "multi_terms": {
                        "terms": [
                          {
                            "field": "genre"
                          },
                          {
                            "field": "product",
                            "missing": "Product Z"
                          }
                        ]
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void nestedAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(match("name", "led tv"))
            .aggregation("resellers",
                nestedAgg("resellers")
                    .aggregation("min_price", min("resellers.price"))
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "match": {
                      "name": "led tv"
                    }
                  },
                  "aggregations": {
                    "resellers": {
                      "aggregations": {
                        "min_price": {
                          "min": {
                            "field": "resellers.price"
                          }
                        }
                      },
                      "nested": {
                        "path": "resellers"
                      }
                    }
                  }
                }
                """
        );
    }


    @Test
    void rangeAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("price_ranges",
                rangeAgg("price",
                    Range.to(100.0f),
                    Range.between(100.f, 200.0f),
                    Range.from(200.0f)
                )
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "price_ranges": {
                      "range": {
                        "field": "price",
                        "ranges": [
                          { "to": 100.0 },
                          { "from": 100.0, "to": 200.0 },
                          { "from": 200.0 }
                        ]
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void rangeAggregationWithKeyed() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("price_ranges",
                rangeAgg("price",
                    Range.to(100.0f),
                    Range.between(100.f, 200.0f),
                    Range.from(200.0f)
                ).withKeyed()
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "price_ranges": {
                      "range": {
                        "field": "price",
                        "ranges": [
                          { "to": 100.0 },
                          { "from": 100.0, "to": 200.0 },
                          { "from": 200.0 }
                        ],
                        "keyed": true
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void rareTermsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("genres",
                rareTerms("genre")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "genres": {
                      "rare_terms": {
                        "field": "genre"
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void rareTermsAggregationWithFiltering() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("genres",
                rareTerms("genre")
                    .withInclude("swi*")
                    .withExclude("electro*")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "genres": {
                      "rare_terms": {
                        "field": "genre",
                        "include": [ "swi*" ],
                        "exclude": [ "electro*" ]
                      }
                    }
                  }
                }
                """
        );
    }

    @Test
    void significantTermsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withQuery(
                terms("force", "British Transport Police")
            )
            .aggregation("significant_crime_types",
                significantTerms("crime_type")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "query": {
                    "terms": { "force": [ "British Transport Police" ] }
                  },
                  "aggregations": {
                    "significant_crime_types": {
                      "significant_terms": { "field": "crime_type" }
                    }
                  }
                }
                """
        );
    }

    @Test
    void termsAggregation() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .aggregation("genres",
                termsAgg("genre")
            )
            .build();

        // When
        var actual = MAPPER.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
            """
                {
                  "aggregations": {
                    "genres": {
                      "terms": { "field": "genre" }
                    }
                  }
                }
                """
        );
    }
}
