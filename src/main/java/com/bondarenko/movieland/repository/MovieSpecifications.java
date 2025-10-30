package com.bondarenko.movieland.repository;

import com.bondarenko.movieland.entity.Movie;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecifications {
    public static Specification<Movie> containsTextInFields(String searchText) {
        return ((root, query, criteriaBuilder) -> {
            if (searchText == null || searchText.isEmpty()) {
                return criteriaBuilder.conjunction();//// WHERE TRUE — без фільтра
            }
            String likePattern = "%" + searchText.toLowerCase() + "%";//SELECT * FROM movies WHERE name_ukrainian LIKE '%man%';

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nameUkrainian")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nameNative")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        });
    }
}
