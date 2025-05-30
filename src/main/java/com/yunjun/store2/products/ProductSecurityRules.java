package com.yunjun.store2.products;

import com.yunjun.store2.common.SecurityRules;
import com.yunjun.store2.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductSecurityRules implements SecurityRules {
    /**
     * @author yunjunwang
     * @since 30/05/2025
     *
     * @param registry
     */
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole(Role.ADMIN.name());
    }
}
