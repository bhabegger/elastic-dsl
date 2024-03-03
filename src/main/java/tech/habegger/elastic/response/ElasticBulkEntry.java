package tech.habegger.elastic.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticBulkEntry {
    private final String _index;
    private final String _id;
    private final Integer _version;
    private final String result;
    private final Map<String, String> shards;

    @JsonCreator
    public ElasticBulkEntry(
        @JsonProperty("_index") String index,
        @JsonProperty("_id") String id,
        @JsonProperty("_version") Integer version,
        @JsonProperty("result") String result,
        @JsonProperty("_shards") Map<String, String> shards) {
        this._index = index;
        this._id = id;
        this._version = version;
        this.result = result;
        this.shards = shards;
    }

    public String getIndex() {
        return _index;
    }

    public String getId() {
        return _id;
    }


}
