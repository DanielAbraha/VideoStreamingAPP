package Kabbe.org.apigatewayservice.dto;

import lombok.Data;

@Data
public class Role {
    private Long roleId;

    private  RoleType roleName = RoleType.USER;

    public Role(RoleType roleName) {
        this.roleName = roleName;
    }

}
