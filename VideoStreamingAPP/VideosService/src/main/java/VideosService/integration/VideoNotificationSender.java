package VideosService.integration;

import VideosService.dto.VideoDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class VideoNotificationSender {

    @Autowired
    private NewTopic uploadVideoTopic;
    @Autowired
    private NewTopic deleteVideoTopic;
    @Autowired
    private KafkaTemplate<String, VideoDto> kafkaTemplate;

    public void sendUploadVideoMessage(VideoDto videoDto){

        Message<VideoDto> message = MessageBuilder
                .withPayload(videoDto)
                .setHeader(KafkaHeaders.TOPIC, uploadVideoTopic.name())
                .build();
        kafkaTemplate.send(message);


        System.out.println("message sent");
    }

    public void sendDeleteVideoMessage(VideoDto videoDto){

        Message<VideoDto> message = MessageBuilder
                .withPayload(videoDto)
                .setHeader(KafkaHeaders.TOPIC, deleteVideoTopic.name())
                .build();
        kafkaTemplate.send(message);


        System.out.println("message sent");
    }

}
