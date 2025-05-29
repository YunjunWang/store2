package com.yunjun.store2.auth;

import com.yunjun.store2.users.UserDto;
import com.yunjun.store2.users.UserMapper;
import com.yunjun.store2.users.UserNotFoundException;
import com.yunjun.store2.users.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public Long getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Override
    public LoginResponse loginUser(LoginRequest request, HttpServletResponse response) throws BadCredentialsException {
        /*var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return null;*/
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail() ,
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        var userDto = userMapper.toDto(user);
        var accessToken = jwtService.generateAccessToken(userDto);
        var refreshToken = jwtService.generateRefreshToken(userDto);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * @return
     */
    @Override
    public UserDto getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    /**
     * @param refreshToken
     * @return
     */
    @Override
    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        var user = userRepository.findById(jwt.getUserId()).orElseThrow(UserNotFoundException::new);
        var userDto = userMapper.toDto(user);
        return jwtService.generateAccessToken(userDto);
    }
}
