package com.manapi.manapiproject.model.milestone;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.manapi.manapiproject.model.util.NamedEntityCreateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MilestoneCreateDto extends NamedEntityCreateDto {
    
    @NotNull
    private Date date;

}
