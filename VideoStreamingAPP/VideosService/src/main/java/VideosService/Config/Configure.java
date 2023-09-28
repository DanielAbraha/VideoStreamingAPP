package VideosService.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Configure {

    @Value("${spring.kafka.uploadVideoTopic}")
    private String videoTopic;

    @Value("${spring.kafka.deleteVideoTopic}")
    private String deleteTopic;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public NewTopic uploadVideoTopic(){
        return TopicBuilder.name(videoTopic)
                .build();
    }

    @Bean
    public NewTopic deleteVideoTopic(){
        return TopicBuilder.name(deleteTopic)
                .build();
    }
}
