package com.manapi.manapigateway.model.projects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.manapi.manapigateway.model.users.User;

import lombok.Data;

@Entity
@Data
@Table(name = "project2roles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({ "username", "imageUid" })
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    /**
     * Represent posible actions user can do. <p>
     * 
     * ROLES : <p>
     * 0 -> OWNER <p>
     * 1 -> ADMIN <p>
     * 2 -> MEMBER <p>
     * 3 -> VISITOR <p>
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    private Integer role;

    private Boolean accepted;

}
