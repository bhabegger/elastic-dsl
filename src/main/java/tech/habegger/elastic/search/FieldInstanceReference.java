package tech.habegger.elastic.search;

public record FieldInstanceReference(
        String index,
        String id,
        String path
) {

    public static FieldInstanceReference dataPoint(String index, String id, String path) {
        return new FieldInstanceReference(index, id, path);
    }

}
