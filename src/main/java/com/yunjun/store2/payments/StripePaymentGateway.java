package com.yunjun.store2.payments;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Builder;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.yunjun.store2.orders.Order;
import com.yunjun.store2.orders.OrderItem;
import com.yunjun.store2.orders.PaymentStatus;
import com.yunjun.store2.orders.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Move all the Stripe Payment-related logic and details into this class.
 *
 */
@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

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

    /**
     * @param request
     * @return
     */
    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) throws PaymentException, OrderNotFoundException {
       try {
           var event = Webhook.constructEvent(request.getPayload(), request.getHeaders().get("stripe-signature"), webhookSecret);

           return switch (event.getType()) {
                   case "payment_intent.succeeded" ->
                       Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                   case "payment_intent.payment_failed" ->
                       Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

                   default -> Optional.empty();
           };
       } catch (SignatureVerificationException e) {
           throw new PaymentException("Invalid signature for webhook event");
       }
    }

    /**
     * event.getType()
     * subscription renew
     * charge -> (Charge) stripeObject
     * payment_intent.* -> (PaymentIntent) stripeObject
     *  ...
     *
     * System.out.println(event.getType());
     * can get the event id and check it is new.
     *
     * If the version of Stripe API used is not compatible with the stripe-java library,
     * event.getDataObjectDeserializer().getObject() can return null.
     * To make sure it won't happen, make sure they are compatible by checking on the stripe-java GitHub repo,
     * and the API is the latest in your Stripe account/Developers (Workbench) overview page.
     * @param event
     * @return
     */
    private Long extractOrderId(Event event) throws PaymentException {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(() -> new PaymentException("Cannot deserialize Stripe object."));
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private LineItem createLineItem(OrderItem item) {
        return LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                // dynamic price data, if setPrice(price: String), it is expecting an ID from Stripe that has been registered
                .setPriceData(createPriceData(item))
                .build();
    }

    private PriceData createPriceData(OrderItem item) {
        return PriceData.builder()
                .setCurrency("usd")
                // Stripe requires the amount to be converted to cent of the currency
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                // dynamic product data, if setProduct(product: String), it is expecting an ID from Stripe that has been registered
                .setProductData(createProductData(item))
                .build();
    }

    private ProductData createProductData(OrderItem item) {
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
                .setCancelUrl(websiteUrl + "/checkout-cancel")
                /* make sure the metadata type is set consistently as the same when retrieve it */
                .setPaymentIntentData(createPaymentIntent(order));
    }

    /**
     * make sure the metadata type is set consistently as the same when retrieve it
     * @param order
     * @return
     */
    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams
                .PaymentIntentData
                .builder()
                .putMetadata("order_id", order.getId().toString())
                .build();
    }
}
