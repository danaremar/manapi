package com.manapi.manapiproject.model.column;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.manapi.manapiproject.model.util.NamedEntityCreateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ColumnCreateDto extends NamedEntityCreateDto {

    @NotNull
    @Min(value = 0)
    private Long order;

    private String action;
    
}
