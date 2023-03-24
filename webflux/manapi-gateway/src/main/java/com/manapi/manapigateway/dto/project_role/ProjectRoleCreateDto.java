package com.manapi.manapigateway.dto.project_role;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleCreateDto {

    @NotBlank
	private String projectId;
	
	@NotBlank
	private String userId;

	@NotBlank
	@Length(max = 15)
	private String role;
    
}
