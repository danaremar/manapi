package com.manapi.manapigateway.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDto {

    @NotBlank
	@Length(max = 15)
	private String username;
	
	@NotBlank
	@Length(max = 50)
	private String password;
    
}
