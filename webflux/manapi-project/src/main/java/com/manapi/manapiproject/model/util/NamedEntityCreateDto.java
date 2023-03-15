package com.manapi.manapiproject.model.util;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NamedEntityCreateDto {

    @NotBlank
    @Length(max = 50)
    private String name;
    
}
