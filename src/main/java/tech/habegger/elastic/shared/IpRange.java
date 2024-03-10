package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record IpRange(String mask, String from, String to, String key) {

    public static IpRange mask(String mask) {
        return new IpRange(mask, null, null, null);
    }
    public static IpRange between(String from, String to) {
        return new IpRange(null, from, to, null);
    }

    public static IpRange from(String from) {
        return new IpRange(null, from, null, null);
    }

    public static IpRange to(String to) {
        return new IpRange(null, null, to, null);
    }

    public IpRange withKey(String key) {
        return new IpRange(null, this.from, this.to, key);
    }
}
