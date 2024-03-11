package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeoRect(
    @JsonProperty("top_left")
    GeoCoord topLeft,
    @JsonProperty("bottom_right")
    GeoCoord bottomRight) {

    public static GeoRect geoRect(double top, double left, double bottom, double right) {
        return geoRect(new GeoCoord(top,left), new GeoCoord(bottom, right));
    }

    public static GeoRect geoRect(GeoCoord topLeft, GeoCoord bottomRight) {
        return new GeoRect(topLeft, bottomRight);
    }

}
