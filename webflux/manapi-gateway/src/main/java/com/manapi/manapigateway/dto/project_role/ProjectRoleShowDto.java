package com.manapi.manapigateway.dto.project_role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleShowDto {

    private String id;

    private String userId;

    private Integer role;

    private Boolean accepted;
    
}
