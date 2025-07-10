package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingReaderTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void parseMappings() throws IOException {
        // Given
        try(InputStream is = MappingReaderTest.class.getResourceAsStream("/mappings/sample-mappings.json")) {

            // When
            Map<String, ElasticSettings> indexSettings = MAPPER.readValue(is, new TypeReference<>() {
            });

            // Then
            assertThat(indexSettings)
                .containsKey("default")
                .extracting("default").isInstanceOfSatisfying(ElasticSettings.class,
                    def -> {
                        assertThat(def.mappings().properties())
                            .extractingByKey("dHash")
                            .isInstanceOfSatisfying(ElasticFieldProperty.class, dHash -> assertThat(dHash.type()).isEqualTo("unsigned_long"))
                        ;
                        assertThat(def.mappings().properties())
                            .extractingByKey("date")
                            .isInstanceOfSatisfying(ElasticFieldProperty.class, date ->
                                assertThat(date)
                                    .isEqualTo(
                                        new ElasticFieldProperty(
                                        "date",
                                        Map.of("keyword", new ElasticFieldProperty("keyword", null,256, null, null)),
                                        null,
                                        null,
                                            null
                                        )
                                    )
                            )
                        ;
                        assertThat(def.mappings().properties())
                            .extractingByKey("geo")
                            .isInstanceOfSatisfying(ElasticObjectProperty.class, geo ->
                                assertThat(geo.properties()).extractingByKey("address")
                                    .isInstanceOfSatisfying(ElasticObjectProperty.class, address ->
                                        assertThat(address.properties()).extractingByKey("country").isEqualTo(
                                            new ElasticFieldProperty("keyword", null, null, null, null)
                                        ))
                            );

                    }
                );


        }
    }
}
