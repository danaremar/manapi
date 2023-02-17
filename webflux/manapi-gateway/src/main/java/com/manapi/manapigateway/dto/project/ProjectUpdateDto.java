package com.manapi.manapigateway.dto.project;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectUpdateDto extends ProjectCreateDto {

    @NotBlank
    private String id;
    
}
