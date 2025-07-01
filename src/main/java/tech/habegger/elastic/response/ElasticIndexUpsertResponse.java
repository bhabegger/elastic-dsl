package tech.habegger.elastic.response;

public record ElasticIndexUpsertResponse(
    Boolean acknowledged,
    ElasticSearchError error,
    Integer status
) {
}
