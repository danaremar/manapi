package com.manapi.manapiproject.model.userstory;

import javax.persistence.Table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.manapi.manapiproject.model.sprint.Sprint;
import com.manapi.manapiproject.model.milestone.Milestone;
import com.manapi.manapiproject.model.epic.Epic;
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
@Table(name = "user_story")
public class UserStory extends NamedEntity {

    @NotNull
    @Min(value = 1L)
    private Long number;

    @NotBlank
    private String asUser;

    @NotBlank
    private String iCan;

    @NotBlank
    private String soThat;

    @OneToOne
    private Milestone milestone;

    @OneToOne
    private Epic epic;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

}
