package com.manapi.manapiproject.model.subtask;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
