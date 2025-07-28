package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.ReviewResponse;
import com.bondarenko.movieland.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReviewMapper {
    List<ReviewResponse> toReviewResponse(List<Review> reviews);
}
