package com.yunjun.store2.payments;

import com.yunjun.store2.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class PaymentSecurityRules implements SecurityRules {
    /**
     * @author yunjunwang
     * @since 30/05/2025
     *
     * @param registry
     * Stripe webhook isn't a human, therefore, needs to allow it here
     */
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, "/api/checkout/webhook").permitAll();
    }
}
