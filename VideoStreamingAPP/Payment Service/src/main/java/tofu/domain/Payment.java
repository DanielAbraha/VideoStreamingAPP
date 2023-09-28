//package tofu.domain;
//
//import com.stripe.model.Subscription;
//import jakarta.validation.constraints.Email;
//import lombok.*;
//import org.hibernate.validator.constraints.CreditCardNumber;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDate;
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Document
//public class Payment {
//    @Id
//    private Long paymentId;
//
//    private Long userId; // to retrieve users
//    private String userName;
//    @Email
//    private String email;
//    private double amount; // payment amount
//    private String currency; // type of currency(USD,Euro)
//    private LocalDate timestamp; // when did it happen
//    private String paymentMethodId; // payment id
//    private String type;
//
//    // credit card info
//    @CreditCardNumber
//    private String cardNumber;
//    private Integer exp_month;
//    private Integer exp_year;
//    private String cvc;
//
//
//
//}
