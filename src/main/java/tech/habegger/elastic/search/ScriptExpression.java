package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScriptExpression(
        ScriptLang lang,
        String source,
        String id,
        Map<String, Object> params) {
    public static ScriptExpression scriptInline(String source) {
        return scriptInline(null, source, null);
    }

    public static ScriptExpression scriptInline(String source, Map<String, Object> params) {
        return scriptInline(null, source, params);
    }

    public static ScriptExpression scriptInline(ScriptLang lang, String source, Map<String, Object> params) {
        return new ScriptExpression(lang, source, null, params);
    }
    public static ScriptExpression scriptReference(String id) {
        return scriptReference(null, id, null);
    }
    public static ScriptExpression scriptReference(String id, Map<String, Object> params) {
        return scriptReference(null, id, params);
    }
    public static ScriptExpression scriptReference(ScriptLang lang, String id, Map<String, Object> params) {
        return new ScriptExpression(lang, null, id, params);
    }


    @SuppressWarnings("unused")
    public enum ScriptLang {
        painless,
        expression,
        mustache,
        java
    }
}