package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.habegger.elastic.shared.ScriptExpression;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonDeserialize(using = ElasticProperty.ElasticPropertyDeserializer.class)
public interface ElasticProperty {
    @SuppressWarnings("unchecked")
    class ElasticPropertyDeserializer extends StdDeserializer<ElasticProperty> {

        @SuppressWarnings("unused")
        public ElasticPropertyDeserializer() {
            this(null);
        }

        @Override
        public ElasticProperty deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            var codec = parser.getCodec();
            Map<String, Object> rawData = new LinkedHashMap<>();
            var token = parser.nextToken();
            while(token == JsonToken.FIELD_NAME) {
                var fieldName = parser.getValueAsString();
                parser.nextToken();
                Object value = switch(fieldName) {
                    case "properties", "fields" -> codec.readValue(parser, new TypeReference<Map<String, ElasticProperty>>(){});
                    case "type", "format" -> codec.readValue(parser, String.class);
                    case "ignore_above" -> codec.readValue(parser, Integer.class);
                    default -> null;
                };
                rawData.put(fieldName, value);
                token = parser.nextToken();
            }
            if(rawData.containsKey("properties")) {
                return new ElasticObjectProperty(
                    (String)rawData.get("type"),
                    (Map<String, ElasticProperty>) rawData.get("properties")
                );
            } else {
                return new ElasticFieldProperty(
                    (String)rawData.get("type"),
                    (Map<String, ElasticFieldProperty>)rawData.get("fields"),
                    (Integer)rawData.get("ignore_above"),
                    (String)rawData.get("format"),
                    (ScriptExpression)rawData.get("script")
                );
            }
        }

        public ElasticPropertyDeserializer(Class<?> vc) {
            super(vc);
        }

    }
}
