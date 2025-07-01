package tech.habegger.elastic.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.habegger.elastic.mapping.ElasticFieldProperty;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.analysis.filters.ElasticShingleTokenFilter.shingleFilter;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.keywordField;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.textField;
import static tech.habegger.elastic.mapping.ElasticMappingsDefinition.mappings;
import static tech.habegger.elastic.analysis.ElasticAnalysisDefinition.analysis;
import static tech.habegger.elastic.analysis.ElasticAnalyzerDefinition.analyzer;
import static tech.habegger.elastic.settings.ElasticIndexSettingsDefinition.index;
import static tech.habegger.elastic.settings.ElasticSettingsDefinition.settings;

public class SettingsWriterTest {

    @Test
    public void settingsWithMappings() throws JsonProcessingException {
        // Given
        var textWithKeyword =
            textField()
                .withField("keyword", keywordField().build())
                .build();
        var dateField = ElasticFieldProperty.dateField()
            .withFormat("YYYY-MM-DD")
            .build();
        var settings = settings()
            .withIndex(index()
                .withShards(6)
                .withReplicas(1)
                .build())
            .withMappings(mappings()
                .withProperty("firstname", textWithKeyword)
                .withProperty("lastname", textWithKeyword)
                .withProperty("birthdate", dateField)
                .build())
            .build();

        // When
        var json = MAPPER.writeValueAsString(settings);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
                "settings": {
                    "index" : {
                        "number_of_shards" : 6,
                        "number_of_replicas" : 1
                    }
                },
                "mappings" : {
                    "properties" : {
                      "firstname" : {
                        "type" : "text",
                        "fields" : {
                          "keyword" : {
                            "type" : "keyword"
                          }
                        }
                      },
                      "lastname" : {
                        "type" : "text",
                        "fields" : {
                          "keyword" : {
                            "type" : "keyword"
                          }
                        }
                      },
                      "birthdate" : {
                        "type" : "date",
                        "format" : "YYYY-MM-DD"
                      }
                    }
                }
            }
            """);
    }

    @Test
    public void settingsWithAnalyzer() throws JsonProcessingException {
        // Given
        var settings = settings()
            .withAnalysis(analysis()
                .withAnalyzer("shingle", analyzer()
                    .withTokenizer("standard")
                    .withFilters("lowercase", "shingle")
                    .build()
                )
                .withFilter("shingle", shingleFilter()
                    .withMinShingleSize(2)
                    .withMaxShingleSize(3)
                    .withOutputUnigrams(true)
                    .build())
                .build())
            .build();

        // When
        var json = MAPPER.writeValueAsString(settings);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
                "settings": {
                    "analysis": {
                        "analyzer": {
                            "shingle": {
                                "tokenizer": "standard",
                                "filter": [
                                    "lowercase",
                                    "shingle"
                                ]
                            }
                        },
                        "filter": {
                            "shingle": {
                                "min_shingle_size": 2,
                                "max_shingle_size": 3,
                                "output_unigrams": true,
                                "type": "shingle"
                            }
                        }
                    }
                }
            }
            """);
    }
}
