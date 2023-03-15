package com.manapi.manapicommon.model.projects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleCreateDto {
	
	@NotNull
	private Long projectId;
	
	@NotBlank
	private String username;

	/**
     * Represent posible actions user can do. <p>
     * 
     * ROLES : <p>
     * 0 -> OWNER <p>
     * 1 -> ADMIN <p>
     * 2 -> MEMBER <p>
     * 3 -> VISITOR <p>
     */
	@Min(value = 0)
	@Max(value = 3)
	private Integer role;
}
