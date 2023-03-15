package com.manapi.manapicommon.model.projects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoleNotAcceptedDto {
	
	private Long id;

	/**
     * Represent posible actions user can do. <p>
     * 
     * ROLES : <p>
     * 0 -> OWNER <p>
     * 1 -> ADMIN <p>
     * 2 -> MEMBER <p>
     * 3 -> VISITOR <p>
     */
	private Integer role;
	
	private String projectName;
}
