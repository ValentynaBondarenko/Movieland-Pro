package com.bondarenko.movieland.mapper;

import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.entity.User;
import com.bondarenko.movieland.entity.dto.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserJWTResponse toUserResponse(User user);

    UserJWTResponse toUserResponse(String userNickname, String token);

    UserDetails toUserDetails(User user);

}
