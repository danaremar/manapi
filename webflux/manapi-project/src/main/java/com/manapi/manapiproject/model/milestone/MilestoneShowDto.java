package com.manapi.manapiproject.model.milestone;

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
public class MilestoneShowDto extends NamedEntityShowDto {

    private Long number;

    private Date date;
    
}
