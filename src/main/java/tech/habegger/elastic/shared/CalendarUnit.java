package tech.habegger.elastic.shared;

@SuppressWarnings("unused")
public enum CalendarUnit {
    minute("m"),
    hour("h"),
    day("d"),
    week("w"),
    month("M"),
    quarter("q"),
    year("y");

    private final String shortSymbol;

    CalendarUnit(String shortSymbol) {
        this.shortSymbol = shortSymbol;
    }

    public String quantity(int amount) {
        return "%d%s".formatted(amount, shortSymbol);
    }
}
