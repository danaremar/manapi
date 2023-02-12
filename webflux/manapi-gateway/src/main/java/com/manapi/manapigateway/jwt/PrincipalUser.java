package com.manapi.manapigateway.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.manapi.manapigateway.model.user.User;

@Data
@NoArgsConstructor
public class PrincipalUser implements UserDetails {

    private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private String password;
	private String email;
	private Boolean active;
	private Date deleteDate;
	private Long failedRetries;

	private Collection<GrantedAuthority> authorities;

	public static PrincipalUser build(User user) {

		// TODO: get authorities
		List<GrantedAuthority> ls = List.of(new SimpleGrantedAuthority("PROJECTS"));

		// transform to principal
		PrincipalUser principalUser = new PrincipalUser();
		principalUser.setId(user.getId());
		principalUser.setUsername(user.getUsername());
		principalUser.setPassword(user.getPassword());
		principalUser.setEmail(user.getEmail());
		principalUser.setActive(user.getActive());
		principalUser.setDeleteDate(user.getDeleteDate());
		principalUser.setFailedRetries(user.getFailedRetries());
		principalUser.setAuthorities(ls);

		return principalUser;
	}

	@Override
	public boolean isAccountNonExpired() {
		return getDeleteDate() == null || getDeleteDate().before(new Date());
	}

	/**
	 * Denotes if user tried to access multiple times
	 * 
	 * @return <code>true</code> if user tried to access less than 10 times
	 */
	@Override
	public boolean isAccountNonLocked() {
		return getFailedRetries() < 10L;
	}

	/**
	 * Never expires. Expiration date was given in JWT auth
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return getActive();
	}    
    
}
