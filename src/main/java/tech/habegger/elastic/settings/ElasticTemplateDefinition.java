package tech.habegger.elastic.settings;

import tech.habegger.elastic.mapping.ElasticMappingsDefinition;

public record ElasticTemplateDefinition(
    ElasticMappingsDefinition mappings,
    ElasticSettingsDefinition settings
) {
    public static Builder template() {
        return new Builder();
    }

    public static class Builder {
        ElasticMappingsDefinition mappings;
        ElasticSettingsDefinition settings;

        public Builder withSettings(ElasticSettingsDefinition settings) {
            this.settings = settings;
            return this;
        }

        public Builder wthMappings(ElasticMappingsDefinition mappings) {
            this.mappings = mappings;
            return this;
        }
    }
}
