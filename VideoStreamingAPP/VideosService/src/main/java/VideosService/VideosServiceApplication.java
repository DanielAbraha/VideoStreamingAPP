package VideosService;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class VideosServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(VideosServiceApplication.class, args);
	}

}
