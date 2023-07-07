package com.manapi.manapiproject.model.column;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
@Table(name = "column")
public class Column extends NamedEntity {
    
    @NotNull
    @Min(value = 0)
    private Long order;

    @NotBlank
    private String action;

    @NotNull
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "column")
    private List<Task> tasks;

}
