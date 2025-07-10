package tech.habegger.elastic.search;

public record ElasticSearchField(String field) {
    public static ElasticSearchField field(String name) {
        return new ElasticSearchField(name);
    }
}
