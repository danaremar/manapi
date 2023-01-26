package com.manapi.manapigateway.model.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserShowDto {

    private String username;

	private String name;

	private String lastName;

	private String email;
	
	private String imageUid;
    
}
