package tech.habegger.elastic.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ElasticTemplateDeclaration(
    @JsonProperty("index_patterns")
    List<String> indexPatterns,
    ElasticTemplateDefinition template,
    Integer priority,
    Integer version,
    @JsonProperty(" allow_auto_create")
    Boolean allowAutoCreate
) {

    public static Builder templateDeclaration() {
        return new Builder();
    }

    public static class Builder {
        List<String> indexPatterns = new ArrayList<>();
        ElasticTemplateDefinition template;
        Integer priority;
        Integer version;
        Boolean allowAutoCreate;

        Builder withPattern(String... pattern) {
            indexPatterns.addAll(Arrays.stream(pattern).toList());
            return this;
        }

        Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        Builder withAllowAutoCreate(boolean allow) {
            this.allowAutoCreate = allow;
            return this;
        }

        Builder withTemplate(ElasticTemplateDefinition template) {
            this.template = template;
            return this;
        }
    }
}
