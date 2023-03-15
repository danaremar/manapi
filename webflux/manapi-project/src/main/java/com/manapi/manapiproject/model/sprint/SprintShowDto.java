package com.manapi.manapiproject.model.sprint;

import java.util.Date;

import com.manapi.manapiproject.model.util.NamedEntityShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SprintShowDto extends NamedEntityShowDto {

    private Long number;

    private Date creationDate;
    
    private Date startDate;

    private Date endDate;

    private Date closeDate;

}
