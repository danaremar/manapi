package com.manapi.manapiproject.model.sprint;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import com.manapi.manapiproject.model.util.NamedEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "sprint", indexes = {@Index(columnList = "number"), @Index(columnList = "active")})
public class Sprint extends NamedEntity {

    @NotBlank
    private String projectId;

    @NotNull
    @Min(value = 1L)
    private Long number;

    /**
     * Date when is created
     */
    @NotNull
    @PastOrPresent
    private Date creationDate;

    /**
     * Date when starts sprint
     */
    private Date startDate;

    /**
     * Date when sprint estimed to end
     */
    private Date endDate;

    /**
     * Date when user ends / close sprint
     */
    private Date closeDate;

    @NotNull
    private Boolean active;

}
