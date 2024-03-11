package tech.habegger.elastic.shared;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public record SortSpec(String field, OrderDirection direction) {
    public static SortSpec desc(String field) {
        return new SortSpec(field, OrderDirection.desc);
    }

    public static SortSpec asc(String field) {
        return new SortSpec(field, OrderDirection.asc);
    }

    public static SortSpec sort(String field) {
        return new SortSpec(field, null);
    }

    public static List<Map<String, OrderSpec>> toOutput(List<SortSpec> sort) {
        if(sort == null) {
            return null;
        }
        return  sort.stream().map(SortSpec::toOutput).toList();
    }

    public static List<Map<String, OrderSpec>> toOutput(SortSpec[] sort) {
       return  Arrays.stream(sort).map(SortSpec::toOutput).toList();
    }

    public Map<String, OrderSpec> toOutput() {
        return Map.of(field, new OrderSpec(direction));
    }
}
