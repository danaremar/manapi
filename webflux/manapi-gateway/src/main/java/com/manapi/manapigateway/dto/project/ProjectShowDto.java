package com.manapi.manapigateway.dto.project;

import java.util.List;

import com.manapi.manapigateway.dto.project_role.ProjectRoleShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectShowDto extends ProjectListDto {
	
	private List<ProjectRoleShowDto> projectRoles;

}
