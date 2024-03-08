package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocumentReference(
    @JsonProperty("_index")
    String index,
    @JsonProperty("_id")
    String id
) {

    public static DocumentReference documentReference(String id) {
        return new DocumentReference(null, id);
    }
    public static DocumentReference documentReference(String index, String id) {
        return new DocumentReference(index, id);
    }

}
