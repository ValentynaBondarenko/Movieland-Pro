package com.bondarenko.movieland.service.auth;

import com.bondarenko.movieland.api.model.UserJWTResponse;
import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.entity.User;
import com.bondarenko.movieland.service.auth.dto.UserDetails;
import com.bondarenko.movieland.mapper.UserMapper;
import com.bondarenko.movieland.repository.UserRepository;
import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final TokenBlacklist blacklistCache;


    @Override
    public UserJWTResponse login(UserRequest userRequest) {
        User user = authenticate(userRequest);

        UserDetails userDetails = userMapper.toUserDetails(user);
        String token = tokenService.generateToken(userDetails);
        UserJWTResponse userResponse = userMapper.toUserResponse(userDetails.nickname(), token);
        log.info("Response {}", userResponse);
        return userResponse;
    }

    @Override
    public void logout(String token) {
        blacklistCache.addToken(token);
    }

    private User authenticate(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        User user = userRepository.findByEmail("ronald.reynolds66@example.com")
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password ");
        }

        return user;
    }

}
