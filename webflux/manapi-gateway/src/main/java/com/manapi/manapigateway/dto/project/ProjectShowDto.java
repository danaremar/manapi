package com.manapi.manapigateway.dto.project;

import java.util.List;

import com.manapi.manapigateway.dto.project_role.ProjectRoleShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectShowDto {

    private Long id;

	private String name;

	private String description;

	private List<ProjectRoleShowDto> projectRoles;

}
