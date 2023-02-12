package com.manapi.manapigateway.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank
	@Length(max = 15)
	private String username;

	@NotBlank
	@Length(max = 20)
	private String firstName;

	@NotBlank
	@Length(max = 50)
	private String lastName;

	@Length(max = 50)
	@Email
	private String email;

	@NotBlank
	@Length(max = 50)
	private String password;

	@NotBlank
	@Length(min = 2, max = 2)
	private String country;
    
}
