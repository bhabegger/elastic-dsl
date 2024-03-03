package tech.habegger.elastic.search;

public record ElasticMatchAllClause(MatchAllBody match_all) implements ElasticSearchClause {
    private static final ElasticMatchAllClause INSTANCE = new ElasticMatchAllClause(new MatchAllBody());
    public static ElasticMatchAllClause matchAll() {
        return INSTANCE;
    }

    private record MatchAllBody() { }
}
