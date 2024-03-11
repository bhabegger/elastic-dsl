package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.OrderDirection;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeoLineAggregation extends ElasticAggregations {

    @JsonProperty("geo_line")
    private final GeoLineBody geoLine;

    ElasticGeoLineAggregation(
        GeoLineBody geoLine
    ) {
        this.geoLine = geoLine;
    }

    public ElasticGeoLineAggregation withIncludeSort() {
        return withBody(original -> new GeoLineBody(
            original.point,
            original.sort,
            true,
            original.sortOrder,
            original.size
        ));
    }

    public ElasticGeoLineAggregation withSortOrder(OrderDirection sortOrder) {
        return withBody(original -> new GeoLineBody(
            original.point,
            original.sort,
            original.includeSort,
            sortOrder,
            original.size
        ));
    }
    public ElasticGeoLineAggregation withSize(int size) {
        return withBody(original -> new GeoLineBody(
            original.point,
            original.sort,
            original.includeSort,
            original.sortOrder,
            size
        ));
    }

    private ElasticGeoLineAggregation withBody(Function<GeoLineBody, GeoLineBody> update) {
        return new ElasticGeoLineAggregation(update.apply(this.geoLine));
    }

    public static ElasticGeoLineAggregation geoLine(String pointField) {
        return new ElasticGeoLineAggregation(new GeoLineBody(new SingleFieldSpec(pointField), null, null, null, null));
    }

    public static ElasticGeoLineAggregation geoLine(String pointField, String sortField) {
        return new ElasticGeoLineAggregation(new GeoLineBody(new SingleFieldSpec(pointField), new SingleFieldSpec(sortField), null, null, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeoLineBody(
        SingleFieldSpec point,
        SingleFieldSpec sort,
        Boolean includeSort,
        OrderDirection sortOrder,
        Integer size) {
    }

    record SingleFieldSpec(String field) { }
}
