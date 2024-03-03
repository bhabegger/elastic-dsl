package tech.habegger.elastic.search;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticFunctionScoreClause(FunctionScoreBody function_score) implements ElasticSearchClause {

record FunctionScoreBody(ElasticSearchClause query, List<FunctionBody> functions) {}

    public static Builder newFunctionScore(ElasticSearchClause query) {
        return new Builder(query);
    }

    private interface FunctionBody {}
    private record FilterFunctionBody(ElasticSearchClause filter, double weight) implements FunctionBody {
    }

    private record RandomScoreFunctionBody(RandomScoreContent random_score) implements FunctionBody {
    }
    private record RandomScoreContent() {
    }

    public static class Builder {
        private final List<FunctionBody> functions = new ArrayList<>();
        private final ElasticSearchClause query;
        public Builder(ElasticSearchClause query) {
            this.query = query;
        }
        public Builder function(ElasticSearchClause filter, double weight) {
            this.functions.add(new FilterFunctionBody(filter, weight));
            return this;
        }
        public Builder randomScore() {
            this.functions.add(new RandomScoreFunctionBody(new RandomScoreContent()));
            return this;
        }

        public ElasticFunctionScoreClause build() {
            return new ElasticFunctionScoreClause(new FunctionScoreBody(query, functions));
        }
    }
}
