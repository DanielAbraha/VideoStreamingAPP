package kabbee.org.notification.config;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Configuration
public class ListnerConfig {

    @Autowired
    private JavaMailSender jms;

    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kabbee@kabbee.org");
        message.setTo(toEmail);
        message.setText(body);

        jms.send(message);
        System.out.println("Mail sent successfully");
    }

    public void sendProfileUpdateEmail(String toEmail, LocalDateTime time){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kabbee@kabbee.org");
        message.setTo(toEmail);
        message.setText(time.toString());

        jms.send(message);
        System.out.println("Mail sent successfully");
    }

    public void sendVideoEmail(List<String> toEmail, String subject, String body){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("tnsu32@gmail.com", "zuhq lhai rfyg rhlh");
            }
        });
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("tnsu32@gmail.com"));
            for(String email : toEmail){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            message.setText(body);
            message.setSubject(subject);
            Transport.send(message);
            System.out.println("Email sent successfully to recipients: " + String.join(", ", toEmail));
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }



    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
