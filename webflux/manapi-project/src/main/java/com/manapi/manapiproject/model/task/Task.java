package com.manapi.manapiproject.model.task;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.manapi.manapiproject.model.effort.Effort;
import com.manapi.manapiproject.model.epic.Epic;
import com.manapi.manapiproject.model.milestone.Milestone;
import com.manapi.manapiproject.model.subtask.Subtask;
import com.manapi.manapiproject.model.userstory.UserStory;
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

    // Milestone / Epic / UserStory

    @OneToOne
    private Milestone milestone;

    @OneToOne
    private Epic epic;

    @OneToOne
    private UserStory userStory;

}
