package com.yunjun.store2.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSession {
    private String checkoutUrl;
}
