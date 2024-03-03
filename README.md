# Elastic Query Java DSL

This project provides a Java DSL library allowing to build elastic queries serializable to the Elastic JSON DSL using Jackson.

For example, instead of having to (cumbersomely) write:

```
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
```

The Java DSL allows to express this as:
```
var query = ElasticSearchRequest.query(
        ElasticBooleanClause.newBool()
            .must(term("firstname", "benjamin"))
            .should(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
            .filter(term("city", "biel"))
            .build()
);
```

## Advantages

* Removes most of the JSON-related boiler plate
* Avoids typos and structural mistakes when writing the queries

## Current query support

This is an initial version of the DSL, therefore all query shapes are not yet supported. However there is a support for *custom* clauses to compensate a bit for the places where support is not there yet. But do feel free to propose a merge request to get the unsupported clauses ;)

### Compound queries

| **Query Type**  | **Supported** | **Tests**                                                                                                                  |
|-----------------|---------------|----------------------------------------------------------------------------------------------------------------------------|
| Boolean         | [x]           | [ElasticSearchBoolQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchBoolQueryTest.java)                   |
| Boosting        | [ ]           |                                                                                                                            |
| Constant score  | [ ]           |                                                                                                                            |
| Disjunction max | [ ]           |                                                                                                                            |
| Function score  | [x]           | [ElasticSearchFunctionScoreQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchFunctionScoreQueryTest.java) |

### Full text queries
| **Query Type**       | **Supported** | **Tests**                                                                                                                         |
|----------------------|---------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Intervals            | [ ]           |                                                                                                                                   |
| Match                | [x]           | [ElasticSearchFullTextQueryTest#matchQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchFullTextQueryTest.java)       |
| Match boolean prefix | [ ]           |                                                                                                                                   |
| Match phrase         | [ ]           |                                                                                                                                   |
| Match phrase prefix  | [x]           | [ElasticSearchFullTextQueryTest#matchPhraseQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchFullTextQueryTest.java) |
| Combined fields      | [ ]           |                                                                                                                                   |
| Multi-match          | [ ]           |                                                                                                                                   |
| Query string         | [ ]           |                                                                                                                                   |
| Simple query string  | [ ]           |                                                                                                                                   |

### Geo queries
| **Query Type**   | **Supported** | **Tests**                                                                                                           |
|------------------|---------------|---------------------------------------------------------------------------------------------------------------------|
| Geo-bounding box | [ ]           |                                                                                                                     |
| Geo-distance     | [ ]           |                                                                                                                     |
| Geo-grid         | [x]           | [ElasticSearchGeoQueryTest#geoHashQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchGeoQueryTest.java) |
| Geo-polygon      | [ ]           |                                                                                                                     |
| Geoshape         | [ ]           |                                                                                                                     |

### Joining queries
| **Query Type**           | **Supported** |
|--------------------------|---------------|
| Nested                   | [ ]           |
| Has child                | [ ]           |
| Has parent               | [ ]           |
| Parent ID                | [ ]           |

### Span queries
| **Query Type**           | **Supported** |
|--------------------------|---------------|
| Span queries             | [ ]           |
| Span containing          | [ ]           |
| Span field masking       | [ ]           |
| Span first               | [ ]           |
| Span multi-term          | [ ]           |
| Span near                | [ ]           |
| Span not                 | [ ]           |
| Span or                  | [ ]           |
| Span term                | [ ]           |
| Span within              | [ ]           |

### Specialized queries
| **Query Type**   | **Supported** | **Tests**                                                                                                                       |
|------------------|---------------|---------------------------------------------------------------------------------------------------------------------------------|
| Distance feature | [ ]           |                                                                                                                                 |
| More like this   | [ ]           |                                                                                                                                 |
| Percolate        | [ ]           |                                                                                                                                 |
| Knn              | [x]           | [ElasticSearchSpecializedQueryTest#knnQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchSpecializedQueryTest.java) |
| Rank feature     | [ ]           |                                                                                                                                 |
| Script           | [ ]           |                                                                                                                                 |
| Script score     | [ ]           |                                                                                                                                 |
| Wrapper          | [ ]           |                                                                                                                                 |
| Pinned Query     | [ ]           |                                                                                                                                 |
| Rule             | [ ]           |                                                                                                                                 |

### Term-level queries
| **Query Type** | **Supported** | **Tests**                                                                                                                      |
|----------------|---------------|--------------------------------------------------------------------------------------------------------------------------------|
| Exists         | [X]           | [ElasticSearchTermLevelQueryTest#existsQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java) |
| Fuzzy          | [ ]           |                                                                                                                                |
| IDs            | [ ]           |                                                                                                                                |
| Prefix         | [x]           | [ElasticSearchTermLevelQueryTest#prefixQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java) |
| Range          | [x]           | [ElasticSearchTermLevelQueryTest#rangeBoth](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java)   |
| Regexp         | [ ]           |                                                                                                                                |
| Term           | [x]           | [ElasticSearchTermLevelQueryTest#termQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java)   |
| Terms          | [x]           | [ElasticSearchTermLevelQueryTest#termsQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java)  |
| Terms set      | [ ]           |                                                                                                                                |
| Wildcard       | [ ]           |                                                                                                                                |

### Other queries
| **Query Type**       | **Supported** | **Tests**                                                                                                                |
|----------------------|---------------|--------------------------------------------------------------------------------------------------------------------------|
| Shape                | [ ]           |                                                                                                                          |
| Match All            | [x]           | [ElasticSearchOtherQueryTest#matchAllQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchOtherQueryTest.java) |
| Text expansion query | [ ]           |                                                                                                                          |

## Current aggregation support

### Bucket aggregations
| **Aggregation Type**                 | **Supported** |
|--------------------------------------|---------------|
| Adjacency matrix                     | [ ]           |
| Auto-interval date histogram         | [x]           |
| Categorize text                      | [ ]           |
| Children                             | [ ]           |
| Composite                            | [ ]           |
| Date histogram                       | [ ]           |
| Date range                           | [ ]           |
| Diversified sampler                  | [ ]           |
| Filter                               | [ ]           |
| Filters                              | [ ]           |
| Frequent item sets                   | [ ]           |
| Geo-distance                         | [ ]           |
| Geohash grid                         | [x]           |
| Geohex grid                          | [ ]           |
| Geotile grid                         | [ ]           |
| Global                               | [ ]           |
| Histogram                            | [ ]           |
| IP prefix                            | [ ]           |
| IP range                             | [ ]           |
| Missing                              | [ ]           |
| Multi Terms                          | [ ]           |
| Nested                               | [ ]           |
| Parent                               | [ ]           |
| Random sampler                       | [ ]           |
| Range                                | [ ]           |
| Rare terms                           | [ ]           |
| Reverse nested                       | [ ]           |
| Sampler                              | [ ]           |
| Significant terms                    | [ ]           |
| Significant text                     | [ ]           |
| Terms                                | [x]           |
| Time series                          | [ ]           |
| Variable width histogram             | [ ]           |
| Subtleties of bucketing range fields | [ ]           |
                                                            
### Metrics aggregations
| **Aggregation Type**      | **Supported** |
|---------------------------|---------------|
| Avg                       | [ ]           |
| Boxplot                   | [ ]           |
| Cardinality               | [ ]           |
| Extended stats            | [ ]           |
| Geo-bounds                | [ ]           |
| Geo-centroid              | [ ]           |
| Geo-line                  | [ ]           |
| Cartesian-bounds          | [ ]           |
| Cartesian-centroid        | [ ]           |
| Matrix stats              | [ ]           |
| Max                       | [x]           |
| Median absolute deviation | [ ]           |
| Min                       | [ ]           |
| Percentile ranks          | [ ]           |
| Percentiles               | [ ]           |
| Rate                      | [ ]           |
| Scripted metric           | [ ]           |
| Stats                     | [x]           |
| String stats              | [ ]           |
| Sum                       | [x]           |
| T-test                    | [ ]           |
| Top hits                  | [x]           |
| Top metrics               | [ ]           |
| Value count               | [ ]           |
| Weighted avg              | [ ]           |

### Pipeline aggregations
| **Aggregation Type**   | **Supported** |
|------------------------|---------------|
| Average bucket         | [ ]           |
| Bucket script          | [ ]           |
| Bucket count K-S test  | [ ]           |
| Bucket correlation     | [ ]           |
| Bucket selector        | [ ]           |
| Bucket sort            | [ ]           |
| Change point           | [ ]           |
| Cumulative cardinality | [ ]           |
| Cumulative sum         | [ ]           |
| Derivative             | [ ]           |
| Extended stats bucket  | [ ]           |
| Inference bucket       | [ ]           |
| Max bucket             | [ ]           |
| Min bucket             | [ ]           |
| Moving function        | [ ]           |
| Moving percentiles     | [ ]           |
| Normalize              | [ ]           |
| Percentiles bucket     | [ ]           |
| Serial differencing    | [ ]           |
| Stats bucket           | [ ]           |
| Sum bucket             | [ ]           |

## Current query response support

The current library also provides a minimal templated support for deserializing Elastic responses.

For example, given the domain model record:
```
record Person(
    String firstname,
    String birthdate,
    String city
) {}
```

Elastic search responses can be parsed using:
```
ObjectMapper mapper = new ObjectMapper();
ElasticSearchResponse<Person> actual = mapper.readValue(rawResponse, new TypeReference<>() {});
```

> **HINT**: Supporting LocalDate for the birthdate field simply requires adding the Java module:
> ```
> <dependency>
>     <groupId>com.fasterxml.jackson.datatype</groupId>
>     <artifactId>jackson-datatype-jsr310</artifactId>
>     <version>2.6.0</version>
> </dependency>
> ```
> and registering it:
> ```
> ObjectMapper mapper = new ObjectMapper();
> mapper.registerModule(new JavaTimeModule());
> ```

## Not yet supported

* Mapping DSL to defined index mappings
* Indexing requests
