package com.yunjun.store2.users;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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
}
