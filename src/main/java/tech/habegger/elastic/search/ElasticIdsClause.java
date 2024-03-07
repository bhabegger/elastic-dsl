package tech.habegger.elastic.search;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticIdsClause(IdsBody ids) implements ElasticSearchClause {
    public static ElasticIdsClause ids(String... ids) {
        return new ElasticIdsClause(new IdsBody(Arrays.asList(ids)));
    }

    private record IdsBody(List<String> values)  {
    }
}
