package VideosService.service;

import VideosService.domain.Video;
import VideosService.dto.UploadVideoResponse;
import VideosService.dto.VideoDto;
import VideosService.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ModelMapper modelMapper;

    

    public VideoDto saveVideo(VideoDto videoDto) {
        System.out.println("Service at entrance");
        Video video = modelMapper.map(videoDto, Video.class);
        var vid=videoRepository.save(video);
        return modelMapper.map(vid,VideoDto.class);
    }



    public VideoDto editVideo(VideoDto videoDto) {
        // Find the video by videoId
        Optional<Video> savedVideo = videoRepository.findById(videoDto.getId());
        // Map the videoDto fields to video
        Video updatedVideo = modelMapper.map(videoDto, Video.class);

        // save the video  to the database
        videoRepository.save(updatedVideo);
        return videoDto;
    }

//    public String uploadThumbnail(MultipartFile file, String videoId) {
//        var savedVideo = getVideoById(videoId);
//
//        String thumbnailUrl = s3Service.uploadFile(file);
//
//        savedVideo.setThumbnailUrl(thumbnailUrl);
//
//        videoRepository.save(savedVideo);
//        return thumbnailUrl;
//    }


    public Optional<Video> getVideoById(String id) {
        return videoRepository.findById(id);
    }


    public Video updateVideo(String id, Video updatedVideo) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            video.setName(updatedVideo.getName());
            video.setDescription(updatedVideo.getDescription());
            // Update any additional relevant information fields

            return videoRepository.save(video);
        } else {
            throw new NoSuchElementException("Video not found with ID: " + id);
        }
    }

    public void deleteVideo(String id) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            videoRepository.delete(video);
        } else {
            throw new NoSuchElementException("Video not found with ID: " + id);
        }
    }

    public VideoDto findVideoByUrl(String url) {

        Video vd=videoRepository.findByVideoUrl(url);
        return modelMapper.map(vd,VideoDto.class);
    }

    public List<VideoDto> getAllVideos() {
        List<Video> videos=   videoRepository.findAll();
        for (Video video : videos) {
            System.out.println(video);
        }
        return videos.stream().map(v->modelMapper.map(v,VideoDto.class)).collect(Collectors.toList());

        }

//  public UploadVideoResponse uploadVideo(MultipartFile file) throws IOException, IllegalStateException {
//        Video video = new Video();
//        video.setName(file.getOriginalFilename());
//        video.setData(file.getBytes());
//        var savedVideo = videoRepository.save(video);
//        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
//
//    }
}
