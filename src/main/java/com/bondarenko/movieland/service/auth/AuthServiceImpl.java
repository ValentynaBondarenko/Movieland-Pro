package com.bondarenko.movieland.service.auth;

import com.bondarenko.movieland.api.dto.UserJWTResponse;
import com.bondarenko.movieland.api.dto.UserRequest;
import com.bondarenko.movieland.entity.User;
import com.bondarenko.movieland.mapper.UserMapper;
import com.bondarenko.movieland.repository.UserRepository;
import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.service.security.TokenService;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        String token = tokenService.generateToken(userRequest.getEmail());

        UserJWTResponse userResponse = userMapper.toUserResponse(user);
        userResponse.token(token);

        return userResponse;
    }


    @Override
    public void logout(String token) {
        blacklistCache.addToken(token);
    }

    private User authenticate(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        User user = userRepository.findByEmail(email)
                .filter(currentUser -> passwordEncoder.matches(password, currentUser.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email " + email + " or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user;
    }

}
