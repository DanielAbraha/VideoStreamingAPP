package Kabbe.org.AuthService.dto;

import Kabbe.org.AuthService.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String email;
    private Role role;
    private String password;
}
