package tech.habegger.elastic.search;

import java.util.ArrayList;
import java.util.List;

import static tech.habegger.elastic.search.DocumentReference.documentReference;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticPinnedClause(PinnedBody pinned) implements ElasticSearchClause {

    public static Builder newPinned(ElasticSearchClause query) {
        return new Builder(query);
    }

    public static class Builder {
        private final List<DocumentReference> docs  = new ArrayList<>();
        private final ElasticSearchClause organic;
        public Builder(ElasticSearchClause organic) {
            this.organic = organic;
        }
        public Builder pin(String id) {
            docs.add(documentReference(id));
            return this;
        }
        public Builder pin(String index, String id) {
            docs.add(documentReference(index, id));
            return this;
        }

        public ElasticPinnedClause build() {
            return new ElasticPinnedClause(new PinnedBody(docs, organic));
        }
    }

    record PinnedBody(List<DocumentReference> docs, ElasticSearchClause organic) {}

}
