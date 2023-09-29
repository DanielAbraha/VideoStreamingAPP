package org.kabbee.integration;

import org.apache.kafka.clients.admin.NewTopic;
import org.kabbee.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationSender {
    @Autowired
    private NewTopic topic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendDeleteAccountMessage(String email){

        Message<String> message = MessageBuilder
                .withPayload(email)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);

        System.out.println("message sent");
    }
}
