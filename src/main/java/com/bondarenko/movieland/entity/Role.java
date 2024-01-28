package com.bondarenko.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");
    private final String name;

    public String getName() {
        return name;
    }
}