package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ArtificialDocument(
        @JsonProperty("_index")
        String indexId,
        @JsonProperty("doc")
        Object doc
) {}
