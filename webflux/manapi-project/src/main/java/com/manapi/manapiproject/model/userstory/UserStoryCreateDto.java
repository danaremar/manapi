package com.manapi.manapiproject.model.userstory;

import javax.validation.constraints.NotBlank;

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
public class UserStoryCreateDto extends NamedEntityCreateDto {
    
    @NotBlank
    @Length(max = 255)
    private String asUser;

    @NotBlank
    @Length(max = 255)
    private String iCan;

    @NotBlank
    @Length(max = 255)
    private String soThat;

    private String milestoneId;

    private String epicId;

}
