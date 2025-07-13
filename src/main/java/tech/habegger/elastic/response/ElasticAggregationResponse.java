package tech.habegger.elastic.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonDeserialize(using = ElasticAggregationResponse.ElasticAggregationResponseDeserializer.class)
public interface ElasticAggregationResponse {
    class ElasticAggregationResponseDeserializer extends StdDeserializer<ElasticAggregationResponse> {

        @SuppressWarnings("unused")
        public ElasticAggregationResponseDeserializer() {
            this(null);
        }

        @Override
        public ElasticAggregationResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            var codec = parser.getCodec();
            Map<String, Object> rawData = new LinkedHashMap<>();
            var token = parser.nextToken();
            boolean inFilter = false;
            while(token == JsonToken.FIELD_NAME) {
                var fieldName = parser.getValueAsString();
                parser.nextToken();
                Object value = switch(fieldName) {
                    case "doc_count" -> {
                        inFilter = true;
                        yield codec.readValue(parser, Long.class);
                    }
                    case "doc_count_error_upper_bound", "sum_other_doc_count", "count" ->
                        codec.readValue(parser, Long.class);
                    case "value" -> codec.readValue(parser, Number.class);
                    case "value_as_string" -> codec.readValue(parser, String.class);
                    case "max", "sum", "avg", "min" -> codec.readValue(parser, Double.class);
                    case "buckets" -> codec.readValue(parser, new TypeReference<List<Map<String, ?>>>(){});
                    default -> {
                        if(inFilter) {
                            yield codec.readValue(parser, ElasticAggregationResponse.class);
                        } else {
                            yield null;
                        }
                    }
                };
                rawData.put(fieldName, value);
                token = parser.nextToken();
            }
            if(inFilter) {
                Map<String, ElasticAggregationResponse> aggregations =
                    rawData.entrySet()
                        .stream()
                        .filter(e -> !"doc_count".equals(e.getKey()))
                        .collect(Collectors.toMap(
                            Map.Entry::getKey, 
                            e -> (ElasticAggregationResponse) e.getValue()
                        ));
                return new ElasticFilterAggregationResponse(
                    (Long)rawData.get("doc_count") ,
                    aggregations
                );
            } else if(rawData.containsKey("buckets")) {
                return new ElasticBucketsAggregationResponse((List<Map<String, ?>>)rawData.get("buckets"));
            } else {
                return new ElasticMetricsAggregationResponse(
                    (Number) rawData.get("value"),
                    (String) rawData.get("value_as_string"),
                    (Long) rawData.get("doc_count_error_upper_bound"),
                    (Long) rawData.get("sum_other_doc_count"),
                    (Long) rawData.get("count"),
                    (Double) rawData.get("max"),
                    (Double) rawData.get("sum"),
                    (Double) rawData.get("avg"),
                    (Double) rawData.get("min")
                );
            }
        }

        public ElasticAggregationResponseDeserializer(Class<?> vc) {
            super(vc);
        }

    }
}
