package tech.habegger.elastic.search;

@SuppressWarnings({"FieldCanBeLocal","unused"})
public record ElasticWrapperClause(WrapperBody wrapper) implements ElasticSearchClause {

    public static ElasticWrapperClause wrapper(String query) {
        return new ElasticWrapperClause(new WrapperBody(query));
    }
    record WrapperBody(String query) {}
}
