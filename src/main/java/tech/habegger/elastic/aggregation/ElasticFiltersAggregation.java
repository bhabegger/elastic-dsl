package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.search.ElasticSearchClause;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticFiltersAggregation extends ElasticAggregations {
    @JsonProperty("filters")
    private final FiltersBody filters;

    ElasticFiltersAggregation(
        @JsonProperty("filters")
        FiltersBody filters

    ) {
        this.filters = filters;
    }

    public static Builder newFilters() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, ElasticSearchClause> filters = new LinkedHashMap<>();
        private Boolean otherBucket;
        private String otherBucketKey;
        private Boolean keyed;

        public Builder filter(String name, ElasticSearchClause query) {
            filters.put(name, query);
            return this;
        }

        public Builder notKeyed() {
            this.keyed = false;
            return this;
        }

        public Builder withOtherBucket() {
            this.otherBucket = true;
            return this;
        }

        public Builder withOtherBucket(String key) {
            // No need to extend the payload with otherBucket set to true as Elastic will set it automatically internally
            this.otherBucketKey = key;
            return this;
        }
        
        public ElasticFiltersAggregation build() {
            return new ElasticFiltersAggregation(new FiltersBody(
                    filters,
                    otherBucket,
                    otherBucketKey,
                    keyed
            ));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record FiltersBody(
            @JsonProperty("filters")
            Map<String, ElasticSearchClause> filters,
            @JsonProperty("other_bucket")
            Boolean otherBucket,
            @JsonProperty("other_bucket_key")
            String otherBucketKey,
            @JsonProperty("keyed")
            Boolean keyed
    ) { }
}
