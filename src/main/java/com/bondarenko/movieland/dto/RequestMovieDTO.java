package com.bondarenko.movieland.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestMovieDTO {
    private int id;
    private String nameUkrainian;

    private String nameNative;

    private int yearOfRelease;

    private String description;

    private double rating;

    private double price;

    private String picturePath;
}
