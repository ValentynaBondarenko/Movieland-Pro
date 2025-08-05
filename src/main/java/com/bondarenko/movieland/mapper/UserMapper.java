package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.entity.User;
import com.bondarenko.movieland.service.security.dto.AuthenticatedUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserJWTResponse toUserResponse(String nickname, String token);

    AuthenticatedUser toUserDetails(User user);

}