package tech.habegger.elastic.shared;

public record GeoCoord(float lat, float lon) {
    public static GeoCoord geoCoord(float lat, float lon) {
        return new GeoCoord(lat, lon);
    }
}
