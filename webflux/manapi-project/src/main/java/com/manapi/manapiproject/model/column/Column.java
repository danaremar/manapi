package com.manapi.manapiproject.model.column;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
