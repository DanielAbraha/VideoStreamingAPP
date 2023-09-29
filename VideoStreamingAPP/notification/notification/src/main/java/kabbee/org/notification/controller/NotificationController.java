package kabbee.org.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users/email")
    public List<String> usersEmail(){
        ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>(){};
        ResponseEntity<List<String>> response = restTemplate.exchange("http://localhost:8091/api/v1/users/email", HttpMethod.GET, null, responseType);
        List<String> emails = response.getBody();
        return emails;
    }
}
