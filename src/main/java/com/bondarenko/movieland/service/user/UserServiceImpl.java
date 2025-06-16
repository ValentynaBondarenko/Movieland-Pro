package com.bondarenko.movieland.service.user;

import com.bondarenko.movieland.api.model.UserRequest;
import com.bondarenko.movieland.api.model.UserResponse;
import com.bondarenko.movieland.entity.User;
import com.bondarenko.movieland.mapper.UserMapper;
import com.bondarenko.movieland.repository.UserRepository;
import com.bondarenko.movieland.service.cache.security.TokenCache;
import com.bondarenko.movieland.util.TokenUtil;
import com.bondarenko.movieland.web.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenCache cache;

    @Override
    public UserResponse login(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email " + email + " or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UUID token = TokenUtil.generateUUID();

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setUuid(token);

        cache.putToken(token);

        return userResponse;
    }

    @Override
    public void logout(UUID uuid) {
        cache.removeToken(uuid);
    }

}
