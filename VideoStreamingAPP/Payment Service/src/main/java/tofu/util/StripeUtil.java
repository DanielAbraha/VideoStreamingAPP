package tofu.util;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tofu.domain.UserData;
import tofu.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripeUtil {
    @Autowired
    PaymentRepository paymentRepository;
    @Value("${secret_stripe_apikey}")
    String stripeKey;

    public UserData getCustomer(String id) throws StripeException {
        Stripe.apiKey = stripeKey;

        Customer customer = Customer.retrieve(id);
        UserData data = setCustomerData(customer);
        paymentRepository.save(data);

        return data;
    }

    public UserData setCustomerData(Customer customer) throws StripeException {
        UserData customerData = new UserData();
        customerData.setCustomerId(customer.getId());
        customerData.setName(customer.getName());
        customerData.setEmail(customer.getEmail());


        return customerData;
    }
    public List<UserData> getAllCustomer() throws StripeException {

        Map<String, Object> params = new HashMap<>();
        params.put("limit",10);

        CustomerCollection customers = Customer.list(params);
        List<UserData> allCustomer = new ArrayList<UserData>();
        for (int i = 0; i < customers.getData().size(); i++) {
            UserData customerData = new UserData();
            customerData.setCustomerId(customers.getData().get(i).getId());
            customerData.setName(customers.getData().get(i).getName());
            customerData.setEmail(customers.getData().get(i).getEmail());
            allCustomer.add(customerData);
          paymentRepository.saveAll(allCustomer);

        }
        return allCustomer;
    }

}
