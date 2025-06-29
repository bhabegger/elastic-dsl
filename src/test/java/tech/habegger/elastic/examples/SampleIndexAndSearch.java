package tech.habegger.elastic.examples;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tech.habegger.elastic.response.ElasticSearchResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static tech.habegger.elastic.analysis.ElasticAnalysisDefinition.*;
import static tech.habegger.elastic.analysis.ElasticAnalyzerDefinition.*;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.*;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.keywordField;
import static tech.habegger.elastic.mapping.ElasticFieldProperty.textField;
import static tech.habegger.elastic.mapping.ElasticMappingsDefinition.*;
import static tech.habegger.elastic.search.ElasticMatchClause.match;
import static tech.habegger.elastic.search.ElasticSearchRequest.requestBuilder;
import static tech.habegger.elastic.settings.ElasticIndexSettingsDefinition.index;
import static tech.habegger.elastic.settings.ElasticSettingsDefinition.settings;

public class SampleIndexAndSearch {
    static final String SEARCH_ENDPOINT = "http://localhost:9200/";
    static final String INDEX_NAME = "playground";
    static final URI ENDPOINT_URI;
    static final URI INDEX_URI;
    static final ObjectMapper MAPPER;
    static {
        try {
            ENDPOINT_URI = new URI(SEARCH_ENDPOINT);
            INDEX_URI = ENDPOINT_URI.resolve(INDEX_NAME + "/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
    }
    static HttpClient CLIENT = HttpClient.newBuilder().build();


    public static void main(String... args) throws IOException, InterruptedException {
        // Define the index settings
        var textFieldWithKeyword = textField().withField("keyword", keywordField().build()).build();
        var dateField = dateField().withFormat("YYYY-MM-DD").build();
        var settings = settings()
            .withIndex(index()
                .withReplicas(0)
                .withShards(2)
                .build())
            .withAnalysis(analysis()
                .withAnalyzer("simple", analyzer()
                    .withTokenizer("standard")
                    .withFilters("lowercase", "asciifolding")
                    .build())
                .build())
            .withMappings(mappings()
                .withProperty("firstname", textFieldWithKeyword)
                .withProperty("lastname", textFieldWithKeyword)
                .withProperty("birthdate", dateField)
                .build())
            .build();

        var settingsJson = MAPPER.writeValueAsString(settings);


        // Delete any existing index
        System.out.printf("Deleting index %s%n", INDEX_NAME);
        var deleteIndexRequest = HttpRequest.newBuilder()
            .DELETE()
            .uri(INDEX_URI)
            .build();
        CLIENT.send(deleteIndexRequest, HttpResponse.BodyHandlers.discarding());

        // Create an index
        System.out.printf("Creating index %s with settings%n======%n%s%n======%n", INDEX_NAME, settingsJson);
        var createIndexRequest = HttpRequest.newBuilder()
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(settingsJson))
            .uri(INDEX_URI)
            .build();
        CLIENT.send(createIndexRequest, HttpResponse.BodyHandlers.discarding());

        // Populate the index
        var indexURI = INDEX_URI
            .resolve("_doc?refresh=wait_for");
        for(var person: new Person[] {
            new Person("Benjamin", "Habegger", LocalDate.of(1977,8,4))
        }) {
            var docJson = MAPPER.writeValueAsString(person);
            System.out.printf("Indexing:%n======%n%s%n======%n", docJson);
            var indexRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(docJson))
                .header("Content-Type", "application/json")
                .uri(indexURI)
                .build();
            var createResponse = CLIENT.send(indexRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(createResponse.body());
        }

        // Search the index
        var searchURI = ENDPOINT_URI.resolve("playground/").resolve("_search");
        var searchRequestBody = requestBuilder().withQuery(match("firstname", "benjamin")).build();
        System.out.printf("Searching index %s with query%n======%n%s%n======%n",INDEX_NAME, searchRequestBody);
        var searchRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(searchRequestBody)))
            .header("Content-Type", "application/json")
            .uri(searchURI)
            .build();
        var responseJson = CLIENT.send(searchRequest, HttpResponse.BodyHandlers.ofString());
        ElasticSearchResponse<Person> response = MAPPER.readValue(responseJson.body(), new TypeReference<>() {});
        System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }

    record Person(
        String firstname,
        String lastname,
        LocalDate birthdate
    ) {}
}
