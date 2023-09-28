package Kabbe.org.apigatewayservice.dto;

import lombok.Data;


public enum RoleType {
    ADMIN,
    CLIENT,
    USER;

    public static RoleType fromString(String role) {
        return RoleType.valueOf(role.toUpperCase());
    }
}
