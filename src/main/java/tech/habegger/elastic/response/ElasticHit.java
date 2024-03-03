package tech.habegger.elastic.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@SuppressWarnings("unused")
public class ElasticHit<T> {
    private final String _index;
    private final String _id;
    private final Double _score;
    private final List<Double> sort;
    private final T _source;

    private final List<String> _ignored;
    @JsonCreator
    public ElasticHit(
        @JsonProperty("_index") String index,
        @JsonProperty("_id") String id,
        @JsonProperty("_score") Double score,
        @JsonProperty("_source") T source,
        @JsonProperty("sort") List<Double> sort,
        @JsonProperty("_ignored") List<String> ignored) {
        _index = index;
        _id = id;
        _score = score;
        _source = source;
        this.sort = sort;
        _ignored = ignored;
    }

    public String getIndex() {
        return _index;
    }

    public String getId() {
        return _id;
    }

    public Double getScore() {
        return _score;
    }

    public Double getSort() {
        return sort != null && !sort.isEmpty() ? sort.get(0): null;
    }
    public T getSource() {
        return _source;
    }

    public List<String> getIgnored() {
        return _ignored;
    }
}
