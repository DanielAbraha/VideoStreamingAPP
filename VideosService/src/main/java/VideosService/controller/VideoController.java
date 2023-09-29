package VideosService.controller;

import VideosService.domain.Video;
import VideosService.dto.VideoDto;
import VideosService.service.VideoService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:80")
@RequestMapping("api/v1/videos")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto addVideo(@RequestBody VideoDto videoDto){
        VideoDto savedVideo=videoService.saveVideo(videoDto);
        System.out.println("Controller");
        return savedVideo;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideo(){
        List<VideoDto> videos=videoService.getAllVideos();
        for (VideoDto video : videos) {
            System.out.println(video);
        }
        return videos;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        Optional<Video> videoOptional = videoService.getVideoById(id);
        return videoOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{url}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoByUrl(@PathVariable("url")String url){
        return videoService.findVideoByUrl(url);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable String id, @RequestBody Video updatedVideo) {
        Video video = videoService.updateVideo(id, updatedVideo);
        return ResponseEntity.ok(video);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return "Video deleted successfully";
    }

//    @GetMapping("/{videoId}")
//    @ResponseStatus(HttpStatus.OK)
//    public VideoDto getVideoDetails(@PathVariable String videoId) {
//        return videoService.getVideoDetails(videoId);
//    }

//     @PostMapping
//     @ResponseStatus(HttpStatus.CREATED)
//     public ResponseEntity<Video> createVideo(@RequestBody Video video) {
//          Video createdVideo = videoService.createVideo(video);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
//        }

//    @PostMapping("/thumbnail")
//    @ResponseStatus(HttpStatus.CREATED)
//    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
//        return videoService.uploadThumbnail(file, videoId);
//    }


    //    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) throws IOException, IllegalStateException {
//        return videoService.uploadVideo(file);
//    }

}
