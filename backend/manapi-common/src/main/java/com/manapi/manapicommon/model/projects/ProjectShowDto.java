package com.manapi.manapicommon.model.projects;

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


}
