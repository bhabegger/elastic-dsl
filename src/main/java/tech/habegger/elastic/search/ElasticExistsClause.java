package tech.habegger.elastic.search;


public record ElasticExistsClause(ExistsBody exists) implements ElasticSearchClause {
    public static ElasticExistsClause exists(String field) {
        return new ElasticExistsClause(new ExistsBody(field));
    }
    private record ExistsBody(String field) {}
}
