package com.manapi.manapiproject.model.epic;

import org.hibernate.validator.constraints.Length;

import com.manapi.manapiproject.model.util.NamedEntityCreateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EpicCreateDto extends NamedEntityCreateDto {
    
    @Length(max = 255)
    private String description;

}
