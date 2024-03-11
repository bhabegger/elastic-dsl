package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeoBoundsAggregation extends ElasticAggregations {

    @JsonProperty("geo_bounds")
    private final GeoBoundsBody geoBounds;

    ElasticGeoBoundsAggregation(
        GeoBoundsBody geoBounds
    ) {
        this.geoBounds = geoBounds;
    }


    public ElasticGeoBoundsAggregation withWrapLongitude() {
        return withBody(original -> new GeoBoundsBody(
            original.field,
            true
        ));
    }

    private ElasticGeoBoundsAggregation withBody(Function<GeoBoundsBody, GeoBoundsBody> update) {
        return new ElasticGeoBoundsAggregation(update.apply(this.geoBounds));
    }

    public static ElasticGeoBoundsAggregation geoBounds(String field) {
        return new ElasticGeoBoundsAggregation(new GeoBoundsBody(field, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeoBoundsBody(
        String field,
        @JsonProperty("wrap_longitude")
        Boolean wrapLongitude) {
    }

}
