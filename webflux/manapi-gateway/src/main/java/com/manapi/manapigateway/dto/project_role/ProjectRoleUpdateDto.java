package com.manapi.manapigateway.dto.project_role;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleUpdateDto {

	@NotBlank
	@Length(max = 15)
	private String role;
}
