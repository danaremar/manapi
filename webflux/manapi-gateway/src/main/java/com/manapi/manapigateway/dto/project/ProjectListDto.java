package com.manapi.manapigateway.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListDto {

    private String id;

	private String name;

	private String description;
    
}
