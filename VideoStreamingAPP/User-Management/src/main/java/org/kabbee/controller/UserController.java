package org.kabbee.controller;

//import org.kabbee.dto.UserDto;
import org.json.JSONObject;
import org.kabbee.dto.UserDto;
import org.kabbee.dto.VideoDTO;
import org.kabbee.entity.SubscriptionRequest;
import org.kabbee.entity.User;
import org.kabbee.integration.UserNotificationSender;
import org.kabbee.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserNotificationSender userNotificationSender;

    private final RestTemplate restTemplate;
   // private final HttpHeaders headers;

    @Autowired
    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody User user){
//        User user = mapper.map(userDto, User.class);
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@RequestBody User updatedUser, @RequestParam String password){
        User user = userService.editUser(password);
        if(user == null){
            System.out.println("Access denied");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            user = userService.addUser(updatedUser);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public String uploadVideo(@RequestBody VideoDTO videoDto){

        restTemplate.postForLocation("http://localhost:8090/api/v1/videos/create", videoDto,  VideoDTO.class);
        return "Video Uploaded Sucessfully";
    }

    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public List<VideoDTO> getVideo() {
        ResponseEntity<List<VideoDTO>> videos = restTemplate.exchange("http://localhost:8090/api/v1/videos",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<VideoDTO>>() {
                });
        if (videos.getStatusCode() == HttpStatus.OK) {
            return videos.getBody();
        } else {
            // Handle error cases
            return Collections.emptyList();
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable String email){
        User user = userService.getUserByEmail(email);
        UserDto userDto = mapper.map(user, UserDto.class);
        userService.deleteUser(email);
        userNotificationSender.sendDeleteAccountMessage(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("")
//    public AuthRequest getUser(@RequestBody AuthRequest authRequest){
//        User user = userService.getUser(username, password);
//        AuthRequest authRequest = mapper.map(user, AuthRequest.class);
//        return authRequest;
//    }

    /**creating subscription from User Service to Payment Service  using rest template*/

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToVideoStreaming(
            @RequestBody SubscriptionRequest subscriptionRequest) {
        String url = "http://localhost:8092/subscription/new";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("cardNumber", subscriptionRequest.getCardNumber());
        requestBody.put("expMonth", subscriptionRequest.getExpMonth());
        requestBody.put("expYear", subscriptionRequest.getExpYear());
        requestBody.put("cvc", subscriptionRequest.getCvc());
        requestBody.put("email", subscriptionRequest.getEmail());
        requestBody.put("priceId", subscriptionRequest.getPriceId());
        requestBody.put("username", subscriptionRequest.getUsername());

        // Create an HttpEntity with the JSON request body and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "sk_test_51NEOelINGiLeLGSIRRhIy9J7wvepHeYqEUoGlWqyBl8BGdL2iBlnC6MWnhpZln7i1fMmFstz6pEiddN31IQnhxX000Joz7EX6N");
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity<>("Subscription successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Subscription failed: " + response.getBody(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Subscription failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




    /** delete subscription from User Service to Payment Service  using rest template*/

    @DeleteMapping("/cancelSubscription/{subscriptionId}")
    public ResponseEntity<String> cancelSubscription(
            @PathVariable String subscriptionId) {
        String url = "http://localhost:8092/subscription/" + subscriptionId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "sk_test_51NEOelINGiLeLGSIRRhIy9J7wvepHeYqEUoGlWqyBl8BGdL2iBlnC6MWnhpZln7i1fMmFstz6pEiddN31IQnhxX000Joz7EX6N");


        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Subscription cancelled successfully");
            return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
        } catch (Exception e) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("error", "Error cancelling subscription: " + e.getMessage());
            return new ResponseEntity<>(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /** updating subscription using rest template from User Service to Payment Service*/

    @PutMapping("/updateSubscription/{subscriptionId}")
    public ResponseEntity<String> updateSubscription(
            @PathVariable String subscriptionId,
            @RequestBody SubscriptionRequest updatedSubscription) {
        String url = "http://localhost:8092/subscription/" + subscriptionId;

        JSONObject requestBody = new JSONObject();
        requestBody.put("cardNumber", updatedSubscription.getCardNumber());
        requestBody.put("expMonth", updatedSubscription.getExpMonth());
        requestBody.put("expYear", updatedSubscription.getExpYear());
        requestBody.put("cvc", updatedSubscription.getCvc());
        requestBody.put("email", updatedSubscription.getEmail());
        requestBody.put("priceId", updatedSubscription.getPriceId());
        requestBody.put("username", updatedSubscription.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "sk_test_51NEOelINGiLeLGSIRRhIy9J7wvepHeYqEUoGlWqyBl8BGdL2iBlnC6MWnhpZln7i1fMmFstz6pEiddN31IQnhxX000Joz7EX6N");

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Subscription updated successfully");
            return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
        } catch (Exception e) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("error", "Error updating subscription: " + e.getMessage());
            return new ResponseEntity<>(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<List<String>> getUsersEmail(){
        List<String> emails = userService.getUsersEmail();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }


}


