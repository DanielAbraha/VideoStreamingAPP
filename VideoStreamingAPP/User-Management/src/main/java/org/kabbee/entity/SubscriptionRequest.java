package org.kabbee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubscriptionRequest {


    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String email;
    private String priceId; // subscription options (1-6-12 months)
    private String username;


    }


