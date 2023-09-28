package tofu.domain;

import lombok.Data;

@Data
public class StripeSubscription {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String email;
    private String priceId; // subscription options (1-6-12 months)
    //private Long numberOfLicense;     // how many people r going to use it
    private String username;
    private boolean success;

}
