---------

[TOC]

---------

# Lightweight User-friendly Elastic/OpenSearch Query Java DSL

This project provides a **usability-first** Java DSL library allowing to build elastic queries serializable to the Elastic JSON DSL using Jackson.
It has user usability and concision in mind. As a DSL it provides type safety and avoids mistakes, but it also has been designed
to be more straightforward in expressing standard situations. 

It's a (quasi) *self-contained* library with no direct dependency to either Elastic or OpenSearch clients or even an 
HTTP client (the choice is yours) and therefore can be integrated seamlessly into your project with a tiny overhead.

It's only non-testing dependency ia jackson to be able to help serialization and deserialization. Usage of Jackson should be reduced 
enough to very stable aspects of it to allow overriding the version without hassle.
All other dependencies (junit and assertJ) are only for testing.

> NOTE: The DSL also works for both *Elastic* and *OpenSearch* as the APIs follow each other closely 
> 
> This DSL is more straight forward than the standard OpenSearch DSL abusing lambdas. It is also somewhat easier than Elastic's
> DSL which strictly follows the API structure (and therefore inherits some of its complexities)
>

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
var query = query(
    newBool()
        .must(term("firstname", "benjamin"))
        .should(range("birthdate", LocalDate.parse("1990-01-01"), LocalDate.parse("2000-01-01")))
        .filter(term("city", "biel"))
    .build()
);
```

## Usage

### Add the maven dependency
```
<dependency>
    <groupId>tech.habegger.elastic</groupId>
    <artifactId>elastic-dsl</artifactId>
    <version>1.0.0</version>
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

>
> For a complete example, checkout [SampleIndexAndSearch](src/test/java/tech/habegger/elastic/examples/SampleIndexAndSearch.java)
> Which demonstrates how to use the DSL with Java's embedded HTTP client on an index named `playground`.
> The example:
> * Creates a settings item using the DSL
> * Deletes the playground index
> * Creates the playground index using the serialization of the DSL settings
> * Pushes an example document (using plain old java record)
> * Creates a query using the DSL
> * Searches the index using the serialized query.
>
> 
### Check out the tests

Most constructs made available through the DSL should have a unit tests. Please have a look in the test suite for example syntax.

## Design

The DSL has been designed with an effort to find a good compromise between completeness (being able to express any Elastic query or aggregation) and conciseness (being able to do so easily).
In order to do this, the following principles have been tried to be followed.

* Mandatory (or very frequently used) parameters are included in the main builder method (e.g. `terms` must have a field name and values so those are passed as direct arguments of the `terms` method).
* Optional less frequent parameters changing the behavior use modifying methods (e.g.  the `boxPlot` aggregation takes the field as single argument and has a modifier method `withCompression` to set the `compression` when needed).
* Only really complex situations use a more advanced "Builder" pattern requiring a final `build()` method call to return the serializable version of the Elastic expression. In this case, the initial building method is prefixed with new. (e.g. `newBool()` starts a bool expression builder).
* In some cases, the initial newXX Builder will take mandatory parameters (e.g. `newPinned` method takes an Elastic clause as argument to define the query of the "organic" documents and differs the "pinning" to later calls)


## Advantages

* Removes most of the JSON-related boilerplate
* Avoids typos and structural mistakes when writing the queries
* Usability driven 
* More straightforward than the API structure (and the official DSLs which strictly follow this structure) 

## Current query support

This is an initial version of the DSL, therefore all query shapes are not yet supported. However, there is a support for *custom* clauses to compensate a bit for the places where support is not there yet. But do feel free to propose a merge request to get the unsupported clauses ;)

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

See test class [ElasticBucketAggregationsTest](src/test/java/tech/habegger/elastic/aggregation/ElasticBucketAggregationsTest.java)

| **Aggregation Type**         | **Supported** | **Tests**                                                             | **Notes** |
|------------------------------|---------------|-----------------------------------------------------------------------|-----------|
| Adjacency matrix             | ✅             | adjacencyMatrixAggregation                                            |           |
| Auto-interval date histogram | ✅             | autoDateHistogramAggregation,...                                      |           |
| Categorize text              | ✅             | categorizeTextAggregation,...                                         |           |
| Children                     | 🔲            |                                                                       |           |
| Composite                    | 🔲            |                                                                       |           |
| Date histogram               | ✅             | dateHistogramWithCalendarInterval, dateHistogramWithFixedInterval,... |           |
| Date range                   | ✅             | dateRangeAggregation, ...                                             |           |
| Diversified sampler          | ✅             | diversifiedSamplerAggregation                                         |           |
| Filter                       | ✅             | filterAggregation                                                     |           |
| Filters                      | ✅             | filtersAggregation                                                    |           |
| Frequent item sets           | ✅             | frequentItemSetsAggregation,...                                       |           |
| Geo-distance                 | ✅             | geoDistanceAggregation,...                                            |           |
| Geohash grid                 | ✅             | geoHashGridAggregation,...                                            |           |
| Geohex grid                  | ✅             | geoHexGridAggregation,...                                             |           |
| Geotile grid                 | ✅             | geoTileGridAggregation,...                                            |           |
| Global                       | ✅             | globalAggregation                                                     |           |
| Histogram                    | ✅             | histogramAggregation,...                                              |           |
| IP prefix                    | ✅             | ipPrefixAggregation,...                                               |           |
| IP range                     | ✅             | ipRangeAggregation,...                                                |           |
| Missing                      | ✅             | missingAggregation                                                    |           |
| Multi Terms                  | ✅             | multiTermsAggregation,...                                             |           |
| Nested                       | ✅             | nestedAggregation                                                     |           |
| Parent                       | 🔲            |                                                                       |           |
| Random sampler               | 🔲            |                                                                       |           |
| Range                        | ✅             | rangeAggregation,...                                                  |           |
| Rare terms                   | ✅             | rareTermsAggregation,...                                              |           |
| Reverse nested               | 🔲            |                                                                       |           |
| Sampler                      | ✅             | samplerAggregation                                                    |           |
| Significant terms            | ✅             | significantTermsAggregation                                           |           |
| Significant text             | ✅             | significantTextAggregation                                            |           |
| Terms                        | ✅             | termsAggregation                                                      |           |
| Time series                  | ✅             | timeSeriesAggregation                                                 |           |
| Variable width histogram     | 🔲            |                                                                       |           |

### [Metrics aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics.html)

See test class [ElasticMetricsAggregationsTest](src/test/java/tech/habegger/elastic/aggregation/ElasticMetricsAggregationsTest.java)

| **Aggregation Type**      | **Supported** | **Tests**                         |
|---------------------------|---------------|-----------------------------------|
| Avg                       | ✅             | avgAggregation                    |
| Boxplot                   | ✅             | boxPlotAggregation,...            |
| Cardinality               | ✅             | cardinalityAggregation            |
| Extended stats            | ✅             | extendedStatsAggregation          |
| Geo-bounds                | ✅             | geoBoundsAggregation              |
| Geo-centroid              | ✅             | geoCentroidAggregation            |
| Geo-line                  | ✅             | geoLineAggregation                |
| Cartesian-bounds          | ✅             | cartesianBoundsAggregation        |
| Cartesian-centroid        | ✅             | cartesianCentroidAggregation      |
| Matrix stats              | ✅             | matrixStatsAggregation            |
| Max                       | ✅             | maxAggregation                    |
| Median absolute deviation | ✅             | medianAbsolutDeviationAggregation |
| Min                       | ✅             | minAggregation                    |
| Percentile ranks          | ✅             | percentileRanksAggregation        |
| Percentiles               | ✅             | percentilesAggregation,...        |
| Rate                      | ✅             | rateAggregation,...               |
| Scripted metric           | 🔲            |                                   |
| Stats                     | ✅             |                                   |
| String stats              | ✅             | stringStatsAggregation,...        |
| Sum                       | ✅             | sumAggregation                    |
| T-test                    | ✅             | tTestAggregation,...              |
| Top hits                  | ✅             | topHitsAggregation                |
| Top metrics               | 🔲            |                                   |
| Value count               | ✅             | valueCountAggregation             |
| Weighted avg              | ✅             | weightAvgAggregation,...          |

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

The current version also provides a minimal templated support for deserializing Elastic responses.

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

## Current index settings support

### Static settings

| **Setting **                              | **Supported** |
|-------------------------------------------|---------------|
| index.number_of_shards                    | ✅             |
| index.number_of_routing_shards            | 🔲            |
| index.codec                               | 🔲            |
| index.routing_partition_size              | 🔲            |
| index.soft_deletes.retention_lease.period | 🔲            |
| index.load_fixed_bitset_filters_eagerly   | 🔲            |
| index.shard.check_on_startup              | 🔲            |

### Dynamic settings

| **Setting **                                   | **Supported** |
|------------------------------------------------|---------------|
| index.number_of_replicas                       | ✅             |
| index.auto_expand_replicas                     | 🔲            |
| index.search.idle.after                        | 🔲            |
| index.refresh_interval                         | ✅             |
| index.max_result_window                        | 🔲            |
| index.max_inner_result_window                  | 🔲            |
| index.max_rescore_window                       | 🔲            |
| index.max_docvalue_fields_search               | 🔲            |
| index.max_script_fields                        | 🔲            |
| index.max_ngram_diff                           | 🔲            |
| index.max_shingle_diff                         | 🔲            |
| index.max_refresh_listeners                    | 🔲            |
| index.analyze.max_token_count                  | 🔲            |
| index.highlight.max_analyzed_offset            | 🔲            |
| index.max_terms_count                          | 🔲            |
| index.max_regex_length                         | 🔲            |
| index.query.default_field                      | 🔲            |
| index.routing.allocation.enable                | 🔲            |
| index.gc_deletes                               | 🔲            |
| index.default_pipeline                         | 🔲            |
| index.final_pipeline                           | 🔲            |
| index.hidden                                   | 🔲            |
| index.dense_vector.hnsw_filter_heuristic       | 🔲            |
| index.esql.stored_fields_sequential_proportion | 🔲            |

## Current analysis definition support

### Customizable Token filters

 | **Token Filter**         | **Supported** |
 |--------------------------|---------------|
 | CJK bigram               | 🔲            |
 | Common grams             | 🔲            |
 | Conditional              | ✅             |
 | Delimited payload        | 🔲            |
 | Dictionary decompounder  | ✅             |
 | Edge n-gram              | 🔲            |
 | Elision                  | 🔲            |
 | Fingerprint              | 🔲            |
 | Flatten graph            | 🔲            |
 | Hunspell                 | 🔲            |
 | Hyphenation decompounder | 🔲            |
 | Keep types               | 🔲            |
 | Keep words               | 🔲            |
 | Keyword marker           | 🔲            |
 | Length                   | 🔲            |
 | Limit token count        | 🔲            |
 | Lowercase                | 🔲            |
 | MinHash                  | 🔲            |
 | Multiplexer              | 🔲            |
 | N-gram                   | 🔲            |
 | Pattern capture          | 🔲            |
 | Pattern replace          | 🔲            |
 | Predicate script         | 🔲            |
 | Shingle                  | ✅             |
 | Stemmer                  | 🔲            |
 | Stemmer override         | 🔲            |
 | Stop                     | 🔲            |
 | Synonym                  | 🔲            |
 | Synonym graph            | 🔲            |
 | Truncate                 | 🔲            |
 | Unique                   | 🔲            |
 | Word delimiter           | 🔲            |
 | Word delimiter graph     | 🔲            |


## Current mapping support

The current version also provides a (still limited) DSL for mapping definitions.

### [Supported field types](https://www.elastic.co/docs/reference/elasticsearch/mapping-reference/field-data-types)
| **Type**                | **Supported** |
|-------------------------|---------------|
| binary                  | ✅             |
| boolean                 | ✅             |
| keyword                 | ✅             |
| constant_keyword        | 🔲            |
| wildcard                | 🔲            |
| long                    | ✅             |
| integer                 | ✅             |
| short                   | ✅             |
| byte                    | ✅             |
| double                  | ✅             |
| float                   | ✅             |
| half_float              | ✅             |
| scaled_float            | ✅             |
| unsigned_long           | ✅             |
| date                    | ✅             |
| date_nanos              | ✅             |
| object                  | ✅             |
| flattened               | 🔲            |
| nested                  | 🔲            |
| join                    | 🔲            |
| passthrough             | 🔲            |
| integer_range           | 🔲            |
| float_range             | 🔲            |
| long_range              | 🔲            |
| double_range            | 🔲            |
| date_range              | 🔲            |
| ip_range                | 🔲            |
| ip                      | 🔲            |
| version                 | 🔲            |
| aggregate_metric_double | 🔲            |
| histogram               | 🔲            |
| text                    | ✅             |
| match_only_text         | 🔲            |
| search_as_you_type      | 🔲            |
| semantic_text           | 🔲            |
| token_count             | 🔲            |
| dense_vector            | 🔲            |
| sparse_vector           | 🔲            |
| rank_feature            | 🔲            |
| rank_features           | 🔲            |
| geo_point               | 🔲            |
| geo_shape               | 🔲            |
| point                   | 🔲            |
| shape                   | 🔲            |

## Not yet supported

* Indexing requests
* Ensuring field compatibility between index mappings and queries (using type-safety)
