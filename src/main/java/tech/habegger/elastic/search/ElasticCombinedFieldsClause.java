package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticCombinedFieldsClause implements ElasticSearchClause {

    @JsonProperty("combined_fields")
    private final CombinedFieldsBody combinedFields;

    ElasticCombinedFieldsClause(CombinedFieldsBody combinedFields) {
        this.combinedFields = combinedFields;
    }

    public ElasticCombinedFieldsClause withAndOperator() {
        var originalBody = this.combinedFields;
        return new ElasticCombinedFieldsClause(
                new CombinedFieldsBody(
                        originalBody.query,
                        originalBody.fields,
                        Operator.and,
                        originalBody.minimumShouldMatch,
                        originalBody.autoGenerateSynonymsPhraseQuery,
                        originalBody.zeroTermsQueryOption)
        );
    }

    public ElasticCombinedFieldsClause withZeroTermsMatchingAll() {
        var originalBody = this.combinedFields;
        return new ElasticCombinedFieldsClause(
                new CombinedFieldsBody(
                        originalBody.query,
                        originalBody.fields,
                        originalBody.operator,
                        originalBody.minimumShouldMatch,
                        originalBody.autoGenerateSynonymsPhraseQuery,
                        ZeroTermsQueryOption.all)
        );
    }

    public ElasticCombinedFieldsClause withMinimumShouldMatch(int minimumShouldMatch) {
        var originalBody = this.combinedFields;
        return new ElasticCombinedFieldsClause(
                new CombinedFieldsBody(
                        originalBody.query,
                        originalBody.fields,
                        originalBody.operator,
                        minimumShouldMatch,
                        originalBody.autoGenerateSynonymsPhraseQuery,
                        originalBody.zeroTermsQueryOption)
        );
    }


    public static ElasticCombinedFieldsClause combinedFields(String queryString, String... fields) {
        return combinedFields(queryString, null, null, null, null, fields);
    }

    public static ElasticCombinedFieldsClause combinedFields(String queryString, Operator operator, String... fields) {
        return combinedFields(queryString, operator, null, null, null, fields);
    }
    public static ElasticCombinedFieldsClause combinedFields(String queryString, Operator operator, Integer minimumShouldMatch, String... fields) {
        return combinedFields(queryString, operator, minimumShouldMatch, null, null, fields);
    }

    public static ElasticCombinedFieldsClause combinedFields(
            String queryString,
            Operator operator,
            Integer minimumShouldMatch,
            Boolean autoGenerateSynonymsPhraseQuery,
            ZeroTermsQueryOption zeroTermsQuery,
            String... fields) {
        return new ElasticCombinedFieldsClause(new CombinedFieldsBody(queryString, Arrays.asList(fields), operator, minimumShouldMatch, autoGenerateSynonymsPhraseQuery, zeroTermsQuery));
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record CombinedFieldsBody(
            String query,
            List<String> fields,
            Operator operator,

            @JsonProperty("minimum_should_match")
            Integer minimumShouldMatch,
            @JsonProperty("auto_generate_synonyms_phrase_query")
            Boolean autoGenerateSynonymsPhraseQuery,
            @JsonProperty("zero_terms_query")
            ZeroTermsQueryOption zeroTermsQueryOption) {
    }

    public enum Operator {
        and,
        or
    }

    public enum ZeroTermsQueryOption {
        all,
        none
    }
}
