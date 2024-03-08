package tech.habegger.elastic.search;

public record ElasticPercolateClause(PercolateBody percolate) implements ElasticSearchClause {
    public static ElasticPercolateClause percolate(String field, Object document) {
        return new ElasticPercolateClause(new PercolateBody(field, document));
    }
    record PercolateBody(String field, Object document) {}
}
