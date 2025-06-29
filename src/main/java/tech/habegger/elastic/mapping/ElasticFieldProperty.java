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

        public Builder withFormat(String format) {
            this.format = format;
            return this;
        }

        public Builder withField(String keyword, ElasticFieldProperty property) {
            this.fields.put(keyword, property);
            return this;
        }
    }

    /*
     * Text fields
     */
    public static Builder keywordField() {
        return new Builder("keyword");
    }

    public static Builder textField() {
        return new Builder("text");
    }

    /*
     * Numerical fields
     */
    public static Builder unsignedLongField() {
        return new Builder("unsigned_long");
    }

    public static Builder longField() {
        return new Builder("long");
    }

    public static Builder integerField() {
        return new Builder("integer");
    }

    public static Builder shortField() {
        return new Builder("short");
    }

    public static Builder byteField() {
        return new Builder("byte");
    }

    public static Builder doubleField() {
        return new Builder("double");
    }

    public static Builder floatField() {
        return new Builder("float");
    }

    public static Builder halfFloatField() {
        return new Builder("half_float");
    }

    public static Builder scaledFloatField() {
        return new Builder("scaled_float");
    }

    public static Builder binaryField() {
        return new Builder("binary");
    }

    public static Builder booleanField() {
        return new Builder("boolean");
    }

    /*
     * Specific scalar fields
     */
    public static Builder ipField() {
        return new Builder("ip");
    }

    public static Builder versionField() {
        return new Builder("version");
    }

    /*
     * Range fields
     */
    public static Builder integerRangeField() {
        return new Builder("integer_range");
    }

    public static Builder floatRangeField() {
        return new Builder("float_range");
    }

    public static Builder longRangeField() {
        return new Builder("long_range");
    }

    public static Builder doubleRangeField() {
        return new Builder("double_range");
    }

    public static Builder dateRangeField() {
        return new Builder("date_range");
    }

    public static Builder ipRangeField() {
        return new Builder("ip_range");
    }

    /*
     * Date fields
     */
    public static Builder dateField() {
        return new Builder("date");
    }

    public static Builder dateNanosField() {
        return new Builder("date_nanos");
    }

    /*
     * Generic field (to allow declaring fields not provided in the DSL)
     */
    public static Builder field(String type) {
        return new Builder(type);
    }
}
