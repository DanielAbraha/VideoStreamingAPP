package kabbee.org.notification.integration;

import kabbee.org.notification.config.ListnerConfig;
import kabbee.org.notification.controller.NotificationController;
import kabbee.org.notification.dto.ProfileChangeEvent;
import kabbee.org.notification.dto.UserDto;
import kabbee.org.notification.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationReceiver {

    private final NotificationController notificationController;
    private static final Logger logger = LoggerFactory.getLogger(NotificationReceiver.class);
    private final ListnerConfig listnerConfig;

    private List<String> usersEmail = new ArrayList<>();
    private UserDto userDto;


    @KafkaListener(topics = "profile-update-topic", groupId = "gid")
    public void ProfileUpdateReceiver(ProfileChangeEvent event){
        try{
            listnerConfig.sendProfileUpdateEmail(event.getEmail(),event.getTimestamp());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.uploadVideoTopic}"
            ,groupId = "${spring.kafka.consumer.group-id}"
    )
    public void videoUploadReceiver(@Payload VideoDto videoDto){
        try{
            System.out.println("New video " + videoDto + " is added.");
            listnerConfig.sendVideoEmail(notificationController.usersEmail(), "New Video", "New video with a title " + videoDto.getTitle() + " is added.");
            System.out.println("New video " + videoDto + " is added.");
        } catch (Exception e){
            logger.error("Error receiving email to : {}", notificationController.usersEmail(), e);
        }

    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.cancelSubscriptionTopic}"
            ,groupId = "${spring.kafka.consumer.group-id}"
    )
    public void paymentReceiver(@Payload String subscription){
        try{
            System.out.println("subscription status: " + subscription);
            listnerConfig.sendEmail("User with user name " + userDto.getEmail(), " subscription status is ", "\"" + subscription + "\"");
            System.out.println("subscription status: " + subscription);
        } catch(Exception e){
            logger.error("Error receiving email to : {}", userDto.getEmail(), e);
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.cancelUserAccountTopic}"
            ,groupId = "${spring.kafka.consumer.group-id}"
    )
    public void userDeleteAccountReceiver(@Payload String email){
        try{
            System.out.println("Account holder with email: " + email + " is deleted");
            listnerConfig.sendEmail( email, " Account deletion : "," Account with email " + email + " is deleted");
            System.out.println("Account holder with email: " + email + " is deleted");
        } catch(Exception e){
            logger.error("Error receiving email to : {}", email, e);
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.deleteVideoTopic}"
            ,groupId = "${spring.kafka.consumer.group-id}"
    )
    public void videoDeleteReceiver(@Payload VideoDto videoDto){
        try{
            System.out.println("Video with title " + videoDto.getTitle() + " is deleted.");
            listnerConfig.sendVideoEmail(notificationController.usersEmail(), " Delete Video", "video with a title " + videoDto.getTitle() + " is deleted.");
            System.out.println("Video with title " + videoDto.getTitle() + " is deleted.");
        } catch (Exception e){
            logger.error("Error receiving email to : {}", notificationController.usersEmail(), e);
        }

    }


}
