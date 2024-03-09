---------

[TOC]

---------

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

## Usage

### At the maven dependency
```
<dependency>
    <groupId>tech.habegger.elastic</groupId>
    <artifactId>elastic-dsl</artifactId>
    <version>0.0.1</version>
</dependency> 
```

### Example

Import the constructs you need (or let your IDE do it for you ;)):

```
import static tech.habegger.elastic.search.ElasticBooleanClause.newBool;
import static tech.habegger.elastic.search.ElasticSearchRequest.query;
import static tech.habegger.elastic.search.ElasticTermClause.term;
```

And just use the DSL:
```
var mapper = new ObjectMapper();
var elasticQuery = query(
    newBool()
        .must(term("lastname", "habegger"))
        .should(term("firstname", "benjamin"))
    .build()
);
var queryAsString = mapper.writeValue(elasticQuery);
```

## Advantages

* Removes most of the JSON-related boiler plate
* Avoids typos and structural mistakes when writing the queries

## Current query support

This is an initial version of the DSL, therefore all query shapes are not yet supported. However there is a support for *custom* clauses to compensate a bit for the places where support is not there yet. But do feel free to propose a merge request to get the unsupported clauses ;)

### [Compound queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/compound-queries.html)

Set test class [ElasticSearchCompoundQueryTest.java](src/test/java/tech/habegger/elastic/search/ElasticSearchCompoundQueryTest.java)

| **Query Type**  | **Supported** | **Tests**           |
|-----------------|---------------|---------------------|
| Boolean         | ✅             | bool*               |
| Boosting        | ✅             | boostingQuery       |
| Constant score  | ✅             | constantScoreQuery  |
| Disjunction max | ✅             | disjunctionMaxQuery |
| Function score  | ✅             | functionScoreQuery  |

### [Full text queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/full-text-queries.html)

See test class [ElasticSearchFullTextQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchFullTextQueryTest.java)

| **Query Type**       | **Supported** | **Test method(s)**     |
|----------------------|---------------|------------------------|
| Intervals            | 🔲            |                        |
| Match                | ✅             | matchQuery             |
| Match boolean prefix | ✅             | matchPhrasePrefixQuery |
| Match phrase         | ✅             | matchBoolPrefixQuery   |
| Match phrase prefix  | ✅             | matchPhraseQuery       |
| Combined fields      | ✅             | combinedFieldsQuery    |
| Multi-match          | ✅             | multiMatchQuery        |
| Query string         | 🔲            |                        |
| Simple query string  | 🔲            |                        |

### [Geo queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-queries.html)

See test class [ElasticSearchGeoQueryTest#geoHashQuery](src/test/java/tech/habegger/elastic/search/ElasticSearchGeoQueryTest.java)

| **Query Type**   | **Supported** | **Tests**                                 |
|------------------|---------------|-------------------------------------------|
| Geo-bounding box | ✅             | geoBoundingBoxQuery                       |
| Geo-distance     | ✅             | geoDistanceQuery                          |
| Geo-grid         | ✅             | geoHashQuery                              |
| Geo-polygon      | ✅             | geoPolygonQuery                           |
| Geoshape         | ✅             | geoShapeInlineQuery, geoShapeIndexedQuery |

### [Joining queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/joining-queries.html)

See test class [ElasticSearchJoinQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchJoinQueryTest.java)

| **Query Type** | **Supported** | **Tests**   |
|----------------|---------------|-------------|
| Nested         | ✅             | nestedQuery |
| Has child      | 🔲            |             |
| Has parent     | 🔲            |             |
| Parent ID      | 🔲            |             |

### [Span queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/span-queries.html)
| **Query Type**     | **Supported** |
|--------------------|---------------|
| Span queries       | 🔲            |
| Span containing    | 🔲            |
| Span field masking | 🔲            |
| Span first         | 🔲            |
| Span multi-term    | 🔲            |
| Span near          | 🔲            |
| Span not           | 🔲            |
| Span or            | 🔲            |
| Span term          | 🔲            |
| Span within        | 🔲            |

### [Specialized queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/specialized-queries.html)

See test class [ElasticSearchSpecializedQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchSpecializedQueryTest.java)

| **Query Type**   | **Supported** | **Tests**                                                  | **Notes**                          |
|------------------|---------------|------------------------------------------------------------|------------------------------------|
| Distance feature | ✅             | distanceFeatureTemporalQuery, distanceFeatureDistanceQuery |                                    |
| More like this   | ✅             | moreLikeThisQuery, moreLikeThisQueryWithInlineDoc          |                                    |
| Percolate        | ✅             | percolateQuery                                             |                                    |
| Knn              | ✅             | knnQuery                                                   |                                    |
| Rank feature     | ✅             | rankFeatureQuery                                           | Missing function object parameters |
| Script           | 🔲            |                                                            |                                    |
| Script score     | ✅             | scriptScoreQuery                                           |                                    |
| Wrapper          | ✅             | wrapperQuery                                               |                                    |
| Pinned Query     | ✅             | pinnedQuery                                                |                                    |
| Rule             | 🔲            |                                                            |                                    |

### [Term-level queries](https://www.elastic.co/guide/en/elasticsearch/reference/current/term-level-queries.html)

See test class [ElasticSearchTermLevelQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchTermLevelQueryTest.java)

| **Query Type** | **Supported** | **Test method(s)**                              |
|----------------|---------------|-------------------------------------------------|
| Exists         | ✅             | existsQuery                                     |
| Fuzzy          | ✅             | fuzzySimple, fuzzyComplex                       |
| IDs            | ✅             | idsQuery                                        |
| Prefix         | ✅             | prefixQuery                                     |
| Range          | ✅             | rangeBoth, rangeQueryGteOnly, rangeQueryLteOnly |
| Regexp         | ✅             | regexpQuerySimple, regexpQueryMultipleFlags     |
| Term           | ✅             | termQuery                                       |
| Terms          | ✅             | termsQuery                                      |
| Terms set      | ✅             | termsSetQueryWithScript                         |
| Wildcard       | ✅             | wildcardQuery                                   |

### Other queries

See test class [ElasticSearchOtherQueryTest](src/test/java/tech/habegger/elastic/search/ElasticSearchOtherQueryTest.java)

| **Query Type**                                                                                              | **Supported** | **Tests**     |
|-------------------------------------------------------------------------------------------------------------|---------------|---------------|
| [Shape](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-shape-query.html)         | 🔲            |               |
| [Match All](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-all-query.html) | ✅             | matchAllQuery |
| Text expansion query                                                                                        | 🔲            |               |

## Current aggregation support

### [Bucket aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket.html)
| **Aggregation Type**                 | **Supported** | **Tests**                                                             |
|--------------------------------------|---------------|-----------------------------------------------------------------------|
| Adjacency matrix                     | ✅             | adjacencyMatrixAggregation                                            |
| Auto-interval date histogram         | ✅             | autoDateHistogramAggregation,...                                      |
| Categorize text                      | ✅             | categorizeTextAggregation,...                                         |
| Children                             | 🔲            |                                                                       |
| Composite                            | 🔲            |                                                                       |
| Date histogram                       | ✅             | dateHistogramWithCalendarInterval, dateHistogramWithFixedInterval,... |
| Date range                           | 🔲            |                                                                       |
| Diversified sampler                  | 🔲            |                                                                       |
| Filter                               | 🔲            |                                                                       |
| Filters                              | 🔲            |                                                                       |
| Frequent item sets                   | 🔲            |                                                                       |
| Geo-distance                         | 🔲            |                                                                       |
| Geohash grid                         | ✅             |                                                                       |
| Geohex grid                          | 🔲            |                                                                       |
| Geotile grid                         | 🔲            |                                                                       |
| Global                               | 🔲            |                                                                       |
| Histogram                            | 🔲            |                                                                       |
| IP prefix                            | 🔲            |                                                                       |
| IP range                             | 🔲            |                                                                       |
| Missing                              | 🔲            |                                                                       |
| Multi Terms                          | 🔲            |                                                                       |
| Nested                               | 🔲            |                                                                       |
| Parent                               | 🔲            |                                                                       |
| Random sampler                       | 🔲            |                                                                       |
| Range                                | 🔲            |                                                                       |
| Rare terms                           | 🔲            |                                                                       |
| Reverse nested                       | 🔲            |                                                                       |
| Sampler                              | 🔲            |                                                                       |
| Significant terms                    | ✅             | significantTermsAggregation                                           |
| Significant text                     | 🔲            |                                                                       |
| Terms                                | ✅             | termsAggregation                                                      |
| Time series                          | 🔲            |                                                                       |
| Variable width histogram             | 🔲            |                                                                       |
| Subtleties of bucketing range fields | 🔲            |                                                                       |

### [Metrics aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics.html)
| **Aggregation Type**      | **Supported** |
|---------------------------|---------------|
| Avg                       | 🔲            |
| Boxplot                   | 🔲            |
| Cardinality               | 🔲            |
| Extended stats            | 🔲            |
| Geo-bounds                | 🔲            |
| Geo-centroid              | 🔲            |
| Geo-line                  | 🔲            |
| Cartesian-bounds          | 🔲            |
| Cartesian-centroid        | 🔲            |
| Matrix stats              | 🔲            |
| Max                       | ✅             |
| Median absolute deviation | 🔲            |
| Min                       | 🔲            |
| Percentile ranks          | 🔲            |
| Percentiles               | 🔲            |
| Rate                      | 🔲            |
| Scripted metric           | 🔲            |
| Stats                     | ✅             |
| String stats              | 🔲            |
| Sum                       | ✅             |
| T-test                    | 🔲            |
| Top hits                  | ✅             |
| Top metrics               | 🔲            |
| Value count               | 🔲            |
| Weighted avg              | 🔲            |

### [Pipeline aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-pipeline.html)
| **Aggregation Type**   | **Supported** |
|------------------------|---------------|
| Average bucket         | 🔲            |
| Bucket script          | 🔲            |
| Bucket count K-S test  | 🔲            |
| Bucket correlation     | 🔲            |
| Bucket selector        | 🔲            |
| Bucket sort            | 🔲            |
| Change point           | 🔲            |
| Cumulative cardinality | 🔲            |
| Cumulative sum         | 🔲            |
| Derivative             | 🔲            |
| Extended stats bucket  | 🔲            |
| Inference bucket       | 🔲            |
| Max bucket             | 🔲            |
| Min bucket             | 🔲            |
| Moving function        | 🔲            |
| Moving percentiles     | 🔲            |
| Normalize              | 🔲            |
| Percentiles bucket     | 🔲            |
| Serial differencing    | 🔲            |
| Stats bucket           | 🔲            |
| Sum bucket             | 🔲            |

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
