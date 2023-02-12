package com.manapi.manapigateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserShowDto {

    private String email;
	
	private String username;

	private String firstName;

	private String lastName;
	
	private String imageUrl;
    
}
