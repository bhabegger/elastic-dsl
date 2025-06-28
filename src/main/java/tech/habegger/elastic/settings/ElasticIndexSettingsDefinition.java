package tech.habegger.elastic.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticIndexSettingsDefinition(
    @JsonProperty("number_of_shards")
    Integer numberOfShards,
    @JsonProperty("number_of_replicas")
    Integer numberOfReplicas,
    @JsonProperty("refresh_interval")
    Integer refreshInterval
) {

    public Builder index() {
        return new Builder();
    }

    public static class Builder {
        Integer numberOfShards;
        Integer numberOfReplicas;
        Long refreshInterval;

        public Builder withShards(int shardCount) {
            numberOfShards = shardCount;
            return this;
        }

        public Builder withReplicas(int replicaCount) {
            numberOfReplicas = replicaCount;
            return this;
        }

        public Builder withRefreshInterval(Duration refreshInterval) {
            this.refreshInterval = refreshInterval.toSeconds();
            return this;
        }
    }
}
