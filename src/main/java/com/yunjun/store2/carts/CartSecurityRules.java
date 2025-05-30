package com.yunjun.store2.carts;

import com.yunjun.store2.common.SecurityRules;
import com.yunjun.store2.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CartSecurityRules implements SecurityRules {
    /**
     * @author yunjunwang
     * @since 30/05/2025
     *
     * @param registry
     */
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/api/carts").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/carts").hasRole(Role.ADMIN.name())
                .requestMatchers( "/api/carts/{cartId}/**").permitAll();
    }
}
