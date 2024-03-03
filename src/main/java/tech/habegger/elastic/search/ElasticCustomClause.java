package tech.habegger.elastic.search;

import java.util.HashMap;
import java.util.Map;

public class ElasticCustomClause extends HashMap<String, Object> implements ElasticSearchClause {
    public static ElasticCustomClause custom(String key, Map<String, Object> body) {
       var clause =  new ElasticCustomClause();
       clause.put(key, body);
       return clause;
    }
}
