package tech.habegger.elastic.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ElasticBulkResponse {
    @JsonProperty("took")
    String took;
    @JsonProperty("errors")
    Boolean errors;
    @JsonProperty("items")
    List<Map<String, ElasticBulkEntry>> items;

    public List<Map<String, ElasticBulkEntry>> getItems() {
        return items;
    }

}
