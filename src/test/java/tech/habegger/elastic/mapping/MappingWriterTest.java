package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.*;
import static tech.habegger.elastic.mapping.ElasticMappingsDefinition.mappings;
import static tech.habegger.elastic.mapping.ElasticObjectProperty.objectProperty;

public class MappingWriterTest {
    @Test
    public void  basicMappings() throws JsonProcessingException {
        // Given
        var keywordSimple = keywordField().build();
        var keyword256 = keywordField().withIgnoreAbove(256).build();
        var textWithKeyword =
            textField()
                .withField("keyword", keyword256)
                .build();
        var dateWithKeyword =
            dateField()
                .withField("keyword", keyword256)
                .build();

        var mappings = mappings()
            .withProperty("dHash", unsignedLongField().build())
            .withProperty("date", dateWithKeyword)
            .withProperty("geo",
                objectProperty()
                    .withProperty("address",
                        objectProperty()
                            .withProperty("ISO3166-2-lvl4", keywordSimple)
                            .withProperty("ISO3166-2-lvl6", keywordSimple)
                            .withProperty("amenity", textWithKeyword)
                            .build())
                    .build())
            .build();

        // When
        var json = MAPPER.writeValueAsString(mappings);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
                "properties": {
                    "dHash": {
                        "type": "unsigned_long"
                    },
                    "date": {
                      "type": "date",
                      "fields": {
                        "keyword": {
                          "type": "keyword",
                          "ignore_above": 256
                        }
                      }
                    },
                    "geo": {
                      "properties": {
                        "address": {
                          "properties": {
                            "ISO3166-2-lvl4": {
                              "type": "keyword"
                            },
                            "ISO3166-2-lvl6": {
                              "type": "keyword"
                            },
                            "amenity": {
                              "type": "text",
                              "fields": {
                                "keyword": {
                                  "type": "keyword",
                                  "ignore_above": 256
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                }
            }
        """);
    }
}
