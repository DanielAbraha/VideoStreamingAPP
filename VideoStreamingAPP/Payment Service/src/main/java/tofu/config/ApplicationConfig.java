package tofu.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Value("${spring.kafka.topic.name}")
    private String paymentTopic;

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(paymentTopic)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
