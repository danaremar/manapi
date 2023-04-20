package com.manapi.manapiproject.model.sprint;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

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
