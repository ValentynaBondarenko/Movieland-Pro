package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.UserResponse;
import com.bondarenko.movieland.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toUserResponse(User user);
}
