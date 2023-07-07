package com.manapi.manapiproject.model.task;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.manapi.manapiproject.model.column.Column;
import com.manapi.manapiproject.model.effort.Effort;
import com.manapi.manapiproject.model.subtask.Subtask;
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
@Table(name = "task")
public class Task extends NamedEntity {

    @NotNull
    @Min(value = 0)
    private Long number;

    @NotBlank
    @Length(max = 255)
    private String description;

    @Min(value = 0)
    private Double estimatedTime;

    @NotNull
    @Min(value = 0)
    private Integer colOrder;

    @Length(max = 50)
    private String importance;

    @Min(value = 0)
    private Long storyPoints;

    @NotNull
    @PastOrPresent
    private Date creationDate;

    private Date startDate;

    private Date endDate;

    @NotNull
    private Boolean active;

    @NotBlank
    @Length(max = 255)
    private String creator;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Column column;

    @ElementCollection
    @CollectionTable(name = "task_assigned_users_id", joinColumns = @JoinColumn(name = "task_id"))
    private List<String> assignedUsersId;

    @ElementCollection
    @CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
    private List<String> tags;

    @ManyToMany
    @JoinTable(name = "task_children")
    @JsonIncludeProperties({ "id", "title", "number", "active" })
    private List<Task> children;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<Subtask> subtasks;

    // EFFORTS

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<Effort> efforts;

    public Double getComputedTime() {
        if (efforts != null && !efforts.isEmpty()) {
            return efforts.stream().mapToDouble(Effort::getTime).sum();
        } else {
            return 0.;
        }
    }

}
