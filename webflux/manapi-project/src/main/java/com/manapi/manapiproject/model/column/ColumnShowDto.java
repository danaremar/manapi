package com.manapi.manapiproject.model.column;

import com.manapi.manapiproject.model.util.NamedEntityShowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ColumnShowDto extends NamedEntityShowDto {

    private Long order;

    private String action;

    private Boolean active;
    
}
