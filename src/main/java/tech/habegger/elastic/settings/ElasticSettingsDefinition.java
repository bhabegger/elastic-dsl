package tech.habegger.elastic.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.analysis.ElasticAnalysisDefinition;
import tech.habegger.elastic.mapping.ElasticMappingsDefinition;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticSettingsDefinition(
    @JsonProperty("settings")
    ElasticSettingsDefinitionBody settingsBody
) {
    public static Builder settings() {
        return new Builder();
    }

    public static class Builder {
        ElasticIndexSettingsDefinition index;
        ElasticMappingsDefinition mappings;
        ElasticAnalysisDefinition analysis;

        public Builder withIndex(ElasticIndexSettingsDefinition index) {
            this.index = index;
            return this;
        }

        public Builder withMappings(ElasticMappingsDefinition mappings) {
            this.mappings = mappings;
            return this;
        }

        public Builder withAnalysis(ElasticAnalysisDefinition analysis) {
            this.analysis = analysis;
            return this;
         }

        public ElasticSettingsDefinition build() {
            return new ElasticSettingsDefinition(new ElasticSettingsDefinitionBody(index, analysis, mappings));
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ElasticSettingsDefinitionBody(
        ElasticIndexSettingsDefinition index,
        ElasticAnalysisDefinition analysis,
        ElasticMappingsDefinition mappings) {
    }
}
