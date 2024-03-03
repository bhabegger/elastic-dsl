package tech.habegger.elastic.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.habegger.elastic.search.ElasticGeoGridClause.geoGrid;

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

}