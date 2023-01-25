package com.manapi.manapigateway.model.users;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users", indexes = { @Index(columnList = "username"), @Index(columnList = "email") })
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(unique = true)
	@Length(max = 15)
	private String username;

	@NotBlank
	@Length(max = 20)
	private String name;

	@NotBlank
	@Length(max = 50)
	private String lastName;

	@Column(unique = true, nullable = false)
	@Length(max = 50)
	@Email
	private String email;

	@JsonIgnore
	@NotBlank
	@Length(max = 256)
	private String password;

	@NotBlank
	@Length(min = 2, max = 2)
	private String country;

	@NotBlank
	@Length(max = 20)
	private String sector;

	@Length(max = 64)
	private String imageUid;

	@JsonIgnore
	@PastOrPresent
	@Column(nullable = false)
	@DateTimeFormat(pattern = "HH:mm:ss dd/MM/yyyy 'GMT'")
	private Date creationDate;

	@JsonIgnore
	@PastOrPresent
	@Column(name = "delete_date")
	@DateTimeFormat(pattern = "HH:mm:ss dd/MM/yyyy 'GMT'")
	private Date deleteDate;

	@PastOrPresent
	@Column(nullable = false)
	@DateTimeFormat(pattern = "HH:mm:ss dd/MM/yyyy 'GMT'")
	private Date lastConnection;
	
	@JsonIgnore
	@Column(nullable = false)
	private Boolean active;
	
	@Min(value = 0L)
	private Long failedRetries;

	@Enumerated(EnumType.STRING)
	private Plan plan;
	
	@Column
	@PastOrPresent
	@DateTimeFormat(pattern = "HH:mm:ss dd/MM/yyyy 'GMT'")
	private Date startPlan;
	
	@Column
	@DateTimeFormat(pattern = "HH:mm:ss dd/MM/yyyy 'GMT'")
	private Date endPlan;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(plan.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		
		// TODO: DELETE DATE < ACTUAL
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		// TODO: ACTIVE
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		// TODO: DELETE DATE < ACTUAL
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// TODO: ACTIVE
		return true;
	}

}
