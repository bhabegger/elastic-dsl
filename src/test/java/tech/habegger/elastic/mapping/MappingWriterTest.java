package tech.habegger.elastic.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.*;
import static tech.habegger.elastic.mapping.ElasticJoinFieldProperty.joinField;
import static tech.habegger.elastic.mapping.ElasticMappingsDefinition.mappings;
import static tech.habegger.elastic.mapping.ElasticObjectProperty.nestedObjectProperty;
import static tech.habegger.elastic.mapping.ElasticObjectProperty.objectProperty;

public class MappingWriterTest {
    ElasticFieldProperty keywordSimple = keywordField().build();
    ElasticFieldProperty keyword256 = keywordField().withIgnoreAbove(256).build();
    ElasticFieldProperty textField = textField().build();
    ElasticFieldProperty keywordField = keywordField().build();
    ElasticFieldProperty textWithKeyword =
        textField()
            .withField("keyword", keyword256)
            .build();
    ElasticFieldProperty dateWithKeyword =
        dateField()
            .withField("keyword", keyword256)
            .build();

    @Test
    public void  basicMappings() throws JsonProcessingException {
        // Given
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

    @Test
    void nestedMapping() throws JsonProcessingException {
        // Given
        var authorField = nestedObjectProperty()
            .withProperty("firstname", textField)
            .withProperty("lastname", textField)
        .build();

        var documentField = objectProperty()
            .withProperty("name", textField)
            .withProperty("authors", authorField)
            .build();

        var mappings = mappings()
            .withProperty("document", documentField)
            .build();


        // When
        var json = MAPPER.writeValueAsString(mappings);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
                "properties": {
                    "document": {
                      "properties": {
                        "name": {
                          "type": "text"
                        },
                        "authors": {
                          "type": "nested",
                          "properties": {
                            "firstname": {
                              "type": "text"
                            },
                            "lastname": {
                              "type": "text"
                            }
                          }
                        }
                      }
                    }
                }
            }
        """);
    }

    @Test
    void joinMapping() throws JsonProcessingException {
        // Given
        var documentField = objectProperty()
            .withProperty("name", textField)
            .build();
        var logicalFileField = objectProperty()
            .withProperty("type", keywordField)
            .build();
        var physicalFileField = objectProperty()
            .withProperty("filename", textWithKeyword)
            .build();

        var mappings = mappings()
            .withProperty("document", documentField)
            .withProperty("logicalFile", logicalFileField)
            .withProperty("physicalFile", physicalFileField)
            .withProperty("relation", joinField()
                .withRelation("document", "logicalFile")
                .withRelation("logicalFile", "physicalFile")
                .build()
            )
            .build();

        // When
        var json = MAPPER.writeValueAsString(mappings);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
              "properties": {
                "document": {
                  "properties": {
                    "name": {
                      "type": "text"
                    }
                  }
                },
                "logicalFile": {
                  "properties": {
                    "type": {
                      "type": "keyword"
                    }
                  }
                },
                "physicalFile": {
                  "properties": {
                    "filename": {
                      "type": "text",
                      "fields": {
                        "keyword": {
                          "type": "keyword",
                          "ignore_above": 256
                        }
                      }
                    }
                  }
                },
                "relation": {
                  "type": "join",
                  "relations": {
                    "document": [ "logicalFile" ],
                    "logicalFile": [ "physicalFile" ]
                  }
                }
              }
            }
        """);
    }
}
