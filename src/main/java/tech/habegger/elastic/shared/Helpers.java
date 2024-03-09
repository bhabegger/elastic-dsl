package tech.habegger.elastic.shared;

import java.util.List;
import java.util.Map;

public class Helpers {
    public static <T> List<T> nullIfEmpty(List<T> clause) {
        if(clause.isEmpty()) {
            return null;
        } else {
            return clause;
        }
    }

    public static <T,S> Map<T,S> nullIfEmpty(Map<T,S> clause) {
        if(clause.isEmpty()) {
            return null;
        } else {
            return clause;
        }
    }
}
