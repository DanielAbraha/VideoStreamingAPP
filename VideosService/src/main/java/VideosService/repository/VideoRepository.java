package VideosService.repository;


import VideosService.domain.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {

    Video findByVideoUrl(String url);
}
