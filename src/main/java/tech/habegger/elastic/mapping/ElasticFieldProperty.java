package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticFieldProperty(
    String type,
    Map<String, ElasticFieldProperty> fields,
    Integer ignore_above,
    String format
) implements ElasticProperty {

    public static class Builder {
        String type;
        Map<String, ElasticFieldProperty> fields = new LinkedHashMap<>();
        Integer ignore_above;
        String format;

        public Builder(String type) {
            this.type = type;
        }

        public ElasticFieldProperty build() {
            return new ElasticFieldProperty(type, fields.isEmpty() ? null : fields, ignore_above, format);
        }

        public Builder withIgnoreAbove(int ignoreAbove) {
            this.ignore_above = ignoreAbove;
            return this;
        }

        public Builder withField(String keyword, ElasticFieldProperty property) {
            this.fields.put(keyword, property);
            return this;
        }
    }

    public static Builder keywordField() {
        return new Builder("keyword");
    }

    public static Builder textField() {
        return new Builder("text");
    }

    public static Builder unsignedLongField() {
        return new Builder("unsigned_long");
    }


    public static Builder dateField() {
        return new Builder("date");
    }
}
