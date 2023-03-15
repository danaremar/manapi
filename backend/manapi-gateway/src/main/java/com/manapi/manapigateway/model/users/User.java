package com.manapi.manapigateway.model.users;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manapi.manapicommon.model.users.PlanType;
import com.manapi.manapigateway.model.projects.ProjectRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users", indexes = { @Index(columnList = "username"), @Index(columnList = "email") })
public class User {

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
	private Date creationDate;

	@JsonIgnore
	@PastOrPresent
	@Column(name = "delete_date")
	private Date deleteDate;

	@PastOrPresent
	@Column(nullable = false)
	private Date lastConnection;

	@JsonIgnore
	@Column(nullable = false)
	private Boolean active;

	@Min(value = 0L)
	private Long failedRetries;

	@Enumerated(EnumType.STRING)
	private PlanType plan;

	@Column
	@PastOrPresent
	private Date startPlan;

	@Column
	private Date endPlan;

	@OneToMany(fetch = FetchType.LAZY)
	private List<Subscription> subscriptions;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<ProjectRole> projectRoles;

}
