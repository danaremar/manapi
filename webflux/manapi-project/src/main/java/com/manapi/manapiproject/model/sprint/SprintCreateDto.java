package com.manapi.manapiproject.model.sprint;

import java.util.Date;

import com.manapi.manapiproject.model.util.NamedEntityCreateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SprintCreateDto extends NamedEntityCreateDto {

    private Date startDate;

    private Date endDate;
    
}
