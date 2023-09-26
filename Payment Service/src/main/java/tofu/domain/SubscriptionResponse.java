package tofu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionResponse {

    @JsonProperty("stripe_customer_id")
    private String stripeCustomerId;

    @JsonProperty("stripe_subscription_id")
    private String stripeSubscriptionId;

    @JsonProperty("stripe_payment_method_id")
    private String stripePaymentMethodId;

    private String username;

}
