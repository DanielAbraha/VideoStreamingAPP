package tofu.integration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationSender {

    @Autowired
    private NewTopic topic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String subscription){

        Message<String> message = MessageBuilder
                .withPayload(subscription)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);


        System.out.println("message sent");
    }
}
