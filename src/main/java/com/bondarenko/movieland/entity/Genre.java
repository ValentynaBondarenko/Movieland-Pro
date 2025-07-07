package com.bondarenko.movieland.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genre {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
