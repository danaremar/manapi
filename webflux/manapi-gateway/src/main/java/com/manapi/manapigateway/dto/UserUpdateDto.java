package com.manapi.manapigateway.dto;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserUpdateDto extends UserCreateDto {

    @NotBlank
	@Length(max = 50)
    private String oldPassword;
    
}
