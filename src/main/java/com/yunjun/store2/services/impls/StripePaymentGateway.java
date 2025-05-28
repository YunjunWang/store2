package com.yunjun.store2.services.impls;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Builder;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.yunjun.store2.entities.Order;
import com.yunjun.store2.entities.OrderItem;
import com.yunjun.store2.services.CheckoutSession;
import com.yunjun.store2.services.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;
    /**
     * @param order
     * @return
     */
    @Override
    public CheckoutSession checkout(Order order) throws StripeException {
        var builder = createSessionBuilder(order);

        order.getItems().forEach(item -> {
            var lineItem = createLineItem(item);
            builder.addLineItem(lineItem);
        });

        var session = Session.create(builder.build());
        return new CheckoutSession(session.getUrl());
    }

    private static LineItem createLineItem(OrderItem item) {
        return LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                // dynamic price data, if setPrice(price: String), it is expecting an ID from Stripe that has been registered
                .setPriceData(createPriceData(item))
                .build();
    }

    private static PriceData createPriceData(OrderItem item) {
        return PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice())
                // dynamic product data, if setProduct(product: String), it is expecting an ID from Stripe that has been registered
                .setProductData(createProductData(item))
                .build();
    }

    private static ProductData createProductData(OrderItem item) {
        return ProductData.builder()
                .setName(item.getProduct().getName())
                // .setDescription(item.getProduct().getDescription())
                .build();
    }

    private Builder createSessionBuilder(Order order) {
        return SessionCreateParams.builder()
                .setMode((SessionCreateParams.Mode.PAYMENT))
                // .setMode((SessionCreateParams.Mode.SUBSCRIPTION))
                .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(websiteUrl + "/checkout-cancel");
    }
}
