package tofu.domain;

import lombok.Data;

@Data
public class StripeToken {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;
    private String username;
    private boolean success;

}
