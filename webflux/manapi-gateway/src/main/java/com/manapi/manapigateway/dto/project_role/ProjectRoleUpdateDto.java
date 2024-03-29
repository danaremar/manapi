package com.manapi.manapigateway.dto.project_role;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleUpdateDto {

	/*
	 * ROLES : 0 -> OWNER; 1 -> ADMIN; 2 -> MEMBER; 3 -> VISITOR
	 */
	@Min(value = 0)
	@Max(value = 3)
	private Integer role;
}
