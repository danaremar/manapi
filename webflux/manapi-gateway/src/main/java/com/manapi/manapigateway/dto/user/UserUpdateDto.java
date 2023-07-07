package com.manapi.manapigateway.dto.user;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
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
