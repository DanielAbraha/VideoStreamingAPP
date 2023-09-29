package tofu.service.paymentservice;


import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import tofu.domain.*;

import java.util.List;

public interface IPaymentService {
    //Payment getPaymentHistory(Long userId);
   // StripeToken createCardToken(StripeToken stripeToken);
   // StripePayment charge(StripePayment chargeRequest);
    SubscriptionResponse createSubscription(StripeSubscription stripeSubscription);
    Subscription cancelSubscription(String subscriptionId) throws StripeException;
      void upgradeSubscription(String subscriptionId, String planId) throws StripeException;

    }
