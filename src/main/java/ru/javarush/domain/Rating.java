package ru.javarush.domain;

public enum Rating {
    PG("PG"),
    G("G"),
    NC17("NC-17"),
    PG13("PG-13"),
    R("R");

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
