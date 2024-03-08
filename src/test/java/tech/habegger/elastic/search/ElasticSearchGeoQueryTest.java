package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticBooleanClause.newBool;
import static tech.habegger.elastic.search.ElasticGeoBoundingBoxClause.geoBoundingBox;
import static tech.habegger.elastic.search.ElasticGeoGridClause.geoGrid;
import static tech.habegger.elastic.search.ElasticMatchAllClause.matchAll;
import static tech.habegger.elastic.search.GeoCoord.geoCoord;

class ElasticSearchGeoQueryTest {

    ObjectMapper mapper = new ObjectMapper();


    @Test
    void geoGridQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
            geoGrid("location", "u0mhhvh3tvu")
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
                "query": {
                    "geo_grid": {
                        "location": {
                            "geohash": "u0mhhvh3tvu"
                        }
                    }
                }
            }
            """
        );
    }

    @Test
    void geoBoundingBoxQuery() throws JsonProcessingException {
        // Given
        var query = ElasticSearchRequest.query(
                newBool()
                    .must(matchAll())
                    .filter(
                        geoBoundingBox(
                        "pin.location",
                            geoCoord(40.73f, -74.1f),
                            geoCoord(40.01f, -71.12f)
                        )
                    )
                .build()
        );

        // When
        var actual = mapper.writeValueAsString(query);

        // Then
        assertThat(actual).isEqualToIgnoringWhitespace(
    """
            {
              "query": {
                "bool": {
                  "must": [
                    {
                        "match_all": {}
                    }
                  ],
                  "filter": [{
                    "geo_bounding_box": {
                      "pin.location": {
                        "top_left": {
                          "lat": 40.73,
                          "lon": -74.1
                        },
                        "bottom_right": {
                          "lat": 40.01,
                          "lon": -71.12
                        }
                      }
                    }
                  }]
                }
              }
            }
            """
        );
    }
}