package com.manapi.manapigateway.dto.project_role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleNotAcceptedDto {

    private String id;

	private String role;
	
	private String projectName;
    
}
