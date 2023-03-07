package com.manapi.manapiproject.model.epic;

import com.manapi.manapiproject.model.util.NamedEntityShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EpicShowDto extends NamedEntityShowDto {

    private Long number;

    private String description;
    
}
