package com.bondarenko.movieland.entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Table(name = "roles")
public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");
    private final String name;
}
