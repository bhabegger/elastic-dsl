package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.search.ElasticSearchClause;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticFrequentItemSetsAggregation extends ElasticAggregations {
    @JsonProperty("frequent_item_sets")
    private final FrequentItemSetBody frequentItemSets;

    ElasticFrequentItemSetsAggregation(
        @JsonProperty("frequent_item_sets")
        FrequentItemSetBody frequentItemSets

    ) {
        this.frequentItemSets = frequentItemSets;
    }
    public ElasticFrequentItemSetsAggregation withSize(Integer size) {
        return withBody((original) ->
                new FrequentItemSetBody(
                    original.fields,
                    original.minimumSupport,
                    original.minimumSetSize,
                    size,
                    original.filter
            ));
    }

    public ElasticFrequentItemSetsAggregation withMinimumSetSize(Integer minimumSetSize) {
        return withBody((original) ->
            new FrequentItemSetBody(
                original.fields,
                original.minimumSupport,
                minimumSetSize,
                original.size,
                original.filter
            ));
    }

    public ElasticFrequentItemSetsAggregation withMinimumSupport(Integer minimumSupport) {
        return withBody((original) ->
                new FrequentItemSetBody(
                    original.fields,
                    minimumSupport,
                    original.minimumSetSize,
                    original.size,
                    original.filter
                ));
    }

    public ElasticFrequentItemSetsAggregation withFilter(ElasticSearchClause filter) {
        return withBody((original) ->
                new FrequentItemSetBody(
                    original.fields,
                    original.minimumSupport,
                    original.minimumSetSize,
                    original.size,
                    filter
                ));
    }
    private ElasticFrequentItemSetsAggregation withBody(Function<FrequentItemSetBody, FrequentItemSetBody> update) {
        return new ElasticFrequentItemSetsAggregation(update.apply(this.frequentItemSets));
    }

    public static ElasticFrequentItemSetsAggregation frequentItemSets(String... fields) {
        return new ElasticFrequentItemSetsAggregation(
                new FrequentItemSetBody(
                        Arrays.stream(fields).map(f -> new FieldSpec(f, null, null)).collect(Collectors.toList()),
                        null, null, null, null
                )
        );
    }

    public static ElasticFrequentItemSetsAggregation frequentItemSets(FieldSpec... fields) {
        return new ElasticFrequentItemSetsAggregation(
                new FrequentItemSetBody(
                        Arrays.asList(fields),
                        null, null, null, null
                )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record FrequentItemSetBody(
            @JsonProperty("fields")
            List<FieldSpec> fields,
            @JsonProperty("minimum_support")
            Integer minimumSupport,
            @JsonProperty("minimum_set_size")
            Integer minimumSetSize,
            @JsonProperty("size")
            Integer size,
            @JsonProperty("filter")
            ElasticSearchClause filter
    ) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DateRange(String from, String to, String key) {
        public static DateRange between(String from, String to) {
            return new DateRange(from, to, null);
        }
        public static DateRange since(String from) {
            return new DateRange(from, null, null);
        }
        public static DateRange until(String to) {
            return new DateRange(null, to, null);
        }
        public DateRange withKey(String key) {
            return new DateRange(this.from, this.to, key);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldSpec(
        String field,
        List<String> include,
        List<String> exclude
    ) {
        public static FieldSpec field(String name, List<String> include, List<String> exclude) {
            return new FieldSpec(name, include, exclude);
        }

        public static FieldSpec field(String name) {
            return new FieldSpec(name, null, null);
        }

        public FieldSpec include(String... values) {
            return new FieldSpec(this.field, Arrays.asList(values), this.exclude);
        }
        public FieldSpec exclude(String... values) {
            return new FieldSpec(this.field, this.include, Arrays.asList(values));
        }
    }
}
