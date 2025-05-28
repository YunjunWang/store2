package com.yunjun.store2.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiSecret;
    }
}
