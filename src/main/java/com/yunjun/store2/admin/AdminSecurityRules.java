package com.yunjun.store2.admin;

import com.yunjun.store2.common.SecurityRules;
import com.yunjun.store2.users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
    /**
     * @author yunjunwang
     * @since 30/05/2025
     *
     * @param registry
     */
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name());
    }
}
