package com.manapi.manapigateway.model.projects;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateDto {

	@NotBlank
	@Length(max = 30)
	private String name;

	@NotBlank
	@Length(max = 255)
	private String description;


}
