package tech.habegger.elastic.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ElasticSearchError(
    String type,
    String reason,
    String index,
    @JsonProperty("resource.type")
    String resourceType,
    @JsonProperty("resource.id")
    String resourceId,
    @JsonProperty("root_cause")
    List<ElasticSearchError> rootCause
) {
}
