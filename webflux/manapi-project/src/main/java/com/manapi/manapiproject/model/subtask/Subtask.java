package com.manapi.manapiproject.model.subtask;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.manapi.manapiproject.model.task.Task;
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
@Table(name = "subtask")
public class Subtask extends NamedEntity {

    @NotNull
    private Boolean done;

    @NotBlank
    @Length(max = 255)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    
}
