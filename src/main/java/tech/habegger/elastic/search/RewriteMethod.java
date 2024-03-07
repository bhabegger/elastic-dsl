package tech.habegger.elastic.search;

@SuppressWarnings("unused")
public enum RewriteMethod {
    constant_score_blended,
    constant_score,
    scoring_boolean,
    top_terms_blended_freqs_N,
    top_terms_boost_N,
    top_terms_N
}
