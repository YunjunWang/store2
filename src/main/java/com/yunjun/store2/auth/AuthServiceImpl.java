package com.yunjun.store2.auth;

import com.yunjun.store2.users.UserNotFoundException;
import com.yunjun.store2.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@AllArgsConstructor
@Service
public class AuthServiceImpl implements UserDetailsService, AuthService {
    private final UserRepository userRepository;

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        var user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User not found")); // give BadCredentialsException rather than UserNotFoundException to prevent information leakage.
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList() // authorities: roles, permissions, etc. leave it empty for now.
        );
    }

    public Long getUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public void loginUser(LoginRequest request) throws BadCredentialsException {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
