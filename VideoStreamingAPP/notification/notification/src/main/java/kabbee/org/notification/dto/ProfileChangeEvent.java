package kabbee.org.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileChangeEvent {

        private String email;
        private LocalDateTime timestamp;

    }
