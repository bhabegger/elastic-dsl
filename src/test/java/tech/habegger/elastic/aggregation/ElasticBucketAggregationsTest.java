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
import static tech.habegger.elastic.aggregation.ElasticSignificantTermsAggregation.significantTerms;
import static tech.habegger.elastic.aggregation.ElasticTermsAggregation.termsAgg;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.search.ElasticTermsClause.terms;
import static tech.habegger.elastic.shared.DateTimeUnit.minute;
import static tech.habegger.elastic.shared.DistanceType.plane;
import static tech.habegger.elastic.shared.DistanceUnit.kilometers;
import static tech.habegger.elastic.shared.GeoCoord.geoCoord;
import static tech.habegger.elastic.shared.GeoRect.geoRect;

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
                    Range.to(100),
                    Range.between(100, 300),
                    Range.from(300)
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
                          { "to": 100 },
                          { "from": 100, "to": 300 },
                          { "from": 300 }
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
                    Range.to(100),
                    Range.between(100, 300),
                    Range.from(300)
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
                          { "to": 100 },
                          { "from": 100, "to": 300 },
                          { "from": 300 }
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
                    Range.to(100000).withKey("first_ring"),
                    Range.between(100000, 300000).withKey("second_ring"),
                    Range.from(300000).withKey("third_ring")
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
                          { "to": 100000, "key": "first_ring" },
                          { "from": 100000, "to": 300000, "key": "second_ring" },
                          { "from": 300000, "key": "third_ring" }
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
