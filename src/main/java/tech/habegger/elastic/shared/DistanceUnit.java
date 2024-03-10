package tech.habegger.elastic.shared;

import com.fasterxml.jackson.annotation.JsonValue;

@SuppressWarnings("unused")
public enum DistanceUnit {
    meters("m"),
    miles("mi"),
    inches("in"),
    yards("yd"),
    kilometers("km"),
    centimeters("cm"),
    millimeters("mm");

    private final String shortSymbol;

    DistanceUnit(String shortSymbol) {
        this.shortSymbol = shortSymbol;
    }


    @JsonValue
    public String symbol() {
        return shortSymbol;
    }
}
