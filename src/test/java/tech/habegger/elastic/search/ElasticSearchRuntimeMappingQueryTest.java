package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.TestUtils.MAPPER;
import static tech.habegger.elastic.aggregation.ElasticHistogramAggregation.histogram;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.doubleField;
import static tech.habegger.elastic.search.ElasticBooleanClause.newBool;
import static tech.habegger.elastic.search.ElasticExistsClause.exists;
import static tech.habegger.elastic.search.ElasticTermClause.term;
import static tech.habegger.elastic.shared.ScriptExpression.ScriptLang.*;
import static tech.habegger.elastic.shared.ScriptExpression.scriptInline;

public class ElasticSearchRuntimeMappingQueryTest {

    @Test
    void runtimeMappingQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.requestBuilder()
            .withRuntimeMapping("logSize",
                doubleField()
                    .withScript(scriptInline(painless, "emit(Math.log10(10*doc['logicalFile.size'].value))")).build())
            .withQuery(
                newBool()
                    .must(term("document.textContent", "habegger"))
                    .mustNot(exists("physicalFile"))
                .build())
            .withAggregations(Map.of(
                "size", histogram("logSize", 0.5)
            ))
            .withField("logSize")
            .build();

        // When
        var json = MAPPER.writeValueAsString(query);

        // Then
        assertThat(json).isEqualToIgnoringWhitespace("""
            {
              "query": {
                "bool": {
                  "must": [
                    {
                      "term": {
                        "document.textContent": "habegger"
                      }
                    }
                  ],
                  "must_not": [
                    {
                      "exists": {
                        "field": "physicalFile"
                      }
                    }
                  ]
                }
              },
              "aggregations": {
                "size": {
                  "histogram": {
                    "field": "logSize",
                    "interval": 0.5
                  }
                }
              },
              "fields": [
                {
                    "field" : "logSize"
                }
              ],
              "runtime_mappings": {
                "logSize": {
                  "type": "double",
                  "script": {
                    "lang": "painless",
                    "source": "emit(Math.log10(10*doc['logicalFile.size'].value))"
                  }
                }
              }
            }
            """);
    }
}
