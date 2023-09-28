package org.kabbee.dto;

import lombok.Data;

import java.util.Set;

@Data
public class VideoDTO {

    private String id;
    private String name;
    private String title;
    private String description;
    private String userId;
    private Set<String> tags;
    private byte[] data;
    private String videoUrl;
}
