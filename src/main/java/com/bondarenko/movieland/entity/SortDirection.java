package com.bondarenko.movieland.entity;

public enum SortDirection {
    ASC,
    DESC;

    public static SortDirection fromString(String value) {
        if (value == null) {
            return null;
        }
        String lowerCaseValue = value.toLowerCase();

        if ("desc".equals(lowerCaseValue)) {
            return DESC;
        } else if ("asc".equals(lowerCaseValue)) {
            return ASC;
        }

        throw new IllegalArgumentException("Invalid SortDirection value: " + value);
    }
}
