package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.dto.UserJWTResponse;
import com.bondarenko.movieland.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserJWTResponse toUserResponse(User user);
}
