package tech.habegger.elastic.shared;

public record GeoCoord(double lat, double lon) {
    public static GeoCoord geoCoord(double lat, double lon) {
        return new GeoCoord(lat, lon);
    }
}
