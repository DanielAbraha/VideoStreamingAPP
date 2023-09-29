package tofu.service.paymentservice;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tofu.domain.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class PaymentService implements IPaymentService{

    @Value("${secret_stripe_apikey}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {

        Stripe.apiKey = stripeApiKey;
    }





//    @Override
//    /**
//     * tokenization is the process Stripe uses to collect sensitive card or
//     * BankAccount details,or personal identifiable information(PII),directly from customers in a secure manner.*/
//    public StripeToken createCardToken(StripeToken stripeToken) {
//        try {
//            Map<String, Object> card = new HashMap<>();
//            card.put("number", stripeToken.getCardNumber());
//            card.put("exp_month", Integer.parseInt(stripeToken.getExpMonth()));
//            card.put("exp_year", Integer.parseInt(stripeToken.getExpYear()));
//            card.put("cvc", stripeToken.getCvc());
//            Map<String, Object> params = new HashMap<>();
//            params.put("card", card);
//            Token token = Token.create(params);
//            if (token != null && token.getId() != null) {
//                stripeToken.setSuccess(true);
//                stripeToken.setToken(token.getId());
//            }
//            return stripeToken;
//        } catch (StripeException e) {
//            log.error("StripeService (createCardToken)", e);
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
//
//
//    @Override
//    public StripePayment charge(StripePayment chargeRequest) {
//        try {
//            chargeRequest.setSuccess(false);
//            Map<String, Object> chargeParams = new HashMap<>();
//            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100));
//            chargeParams.put("currency", "USD");
//            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
//            chargeParams.put("source", chargeRequest.getStripeToken());
//            Map<String, Object> metaData = new HashMap<>();
//            metaData.put("id", chargeRequest.getChargeId());
//            metaData.putAll(chargeRequest.getAdditionalInfo());
//            chargeParams.put("metadata", metaData);
//            Charge charge = Charge.create(chargeParams);
//            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
//
//            if (charge.getPaid()) {
//                chargeRequest.setChargeId(charge.getId());
//                chargeRequest.setSuccess(true);
//
//            }
//            return chargeRequest;
//        } catch (StripeException e) {
//            log.error("StripeService (charge)", e);
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }


    @Override
    public SubscriptionResponse createSubscription(StripeSubscription stripeSubscription) {
        PaymentMethod paymentMethod = createPaymentMethod(stripeSubscription);
        Customer customer = createCustomer(paymentMethod, stripeSubscription);

        paymentMethod = attachCustomerToPaymentMethod(customer, paymentMethod);
        Subscription subscription = createSubscription(stripeSubscription, paymentMethod, customer);

        return createResponse(stripeSubscription, paymentMethod, customer, subscription);
    }

    private SubscriptionResponse createResponse(StripeSubscription subscriptionDto, PaymentMethod paymentMethod, Customer customer, Subscription subscription) {


        return SubscriptionResponse.builder()
                .username(subscriptionDto.getUsername())
                .stripePaymentMethodId(paymentMethod.getId())
                .stripeSubscriptionId(subscription.getId())
                .stripeCustomerId(customer.getId())
                .build();
    }

    private PaymentMethod createPaymentMethod(StripeSubscription stripeSubscription) {

        try {

            Map<String, Object> card = new HashMap<>();

            card.put("number", stripeSubscription.getCardNumber());
            card.put("exp_month", Integer.parseInt(stripeSubscription.getExpMonth()));
            card.put("exp_year", Integer.parseInt(stripeSubscription.getExpYear()));
            card.put("cvc", stripeSubscription.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("type", "card");
            params.put("card", card);

            return PaymentMethod.create(params);

        } catch (StripeException e) {
            log.error("StripeService (createPaymentMethod)", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private Customer createCustomer(PaymentMethod paymentMethod, StripeSubscription stripeSubscription) {

        try {

            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("name", stripeSubscription.getUsername());
            customerMap.put("email", stripeSubscription.getEmail());
            customerMap.put("payment_method", paymentMethod.getId());

            return Customer.create(customerMap);
        } catch (StripeException e) {
            log.error("StripeService (createCustomer)", e);
            throw new RuntimeException(e.getMessage());
        }

    }

    private PaymentMethod attachCustomerToPaymentMethod(Customer customer, PaymentMethod paymentMethod) {

        try {

            paymentMethod = com.stripe.model.PaymentMethod.retrieve(paymentMethod.getId());

            Map<String, Object> params = new HashMap<>();
            params.put("customer", customer.getId());
            paymentMethod = paymentMethod.attach(params);
            return paymentMethod;


        } catch (StripeException e) {
            log.error("StripeService (attachCustomerToPaymentMethod)", e);
            throw new RuntimeException(e.getMessage());
        }

    }

    private Subscription createSubscription(StripeSubscription stripeSubscription, PaymentMethod paymentMethod, Customer customer) {

        try {

            List<Object> items = new ArrayList<>();
            Map<String, Object> item1 = new HashMap<>();
            item1.put(
                    "price",
                    stripeSubscription.getPriceId()
            );

            items.add(item1);

            Map<String, Object> params = new HashMap<>();
            params.put("customer", customer.getId());
            params.put("default_payment_method", paymentMethod.getId());
            params.put("items", items);
            return Subscription.create(params);
        } catch (StripeException e) {
            log.error("StripeService (createSubscription)", e);
            throw new RuntimeException(e.getMessage());
        }

    }


    @Override
    public Subscription cancelSubscription(String subscriptionId) {
        try {
            Subscription retrieve = Subscription.retrieve(subscriptionId);
            return retrieve.cancel();
        } catch (StripeException e) {

            log.error("StripeService (cancelSubscription)", e);
        }

        return null;
    }

    @Override
    public void upgradeSubscription(String subscriptionId, String orderId) throws StripeException {

//        Subscription subscription = Subscription.retrieve(subscriptionId);
//        // Create a new HashMap to store metadata
//        Map<String,Object> metadata = new HashMap<>();
//        metadata.put("planId",orderId);
//
//        // Create a new HashMap to store parameters for updating the subscription
//         Map<String,Object> params = new HashMap<>();
//         params.put("metadata",metadata);
//
//        // Update the subscription with the new metadata
//            subscription.update(params);




                Subscription subscription = Subscription.retrieve(subscriptionId);
                SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                        .addItem(SubscriptionUpdateParams.Item.builder()
                                .setId(subscription.getItems().getData().get(0).getId())
                                .setPrice(orderId)
                                .build())
                        .build();

                subscription.update(params);
            }
        }
















