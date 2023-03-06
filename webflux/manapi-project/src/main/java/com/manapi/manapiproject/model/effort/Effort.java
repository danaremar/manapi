package com.manapi.manapiproject.model.effort;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
@Table(name = "effort")
public class Effort extends NamedEntity {

    private Date startDate;

    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @NotBlank
    @Length(max = 255)
    private String userId;

    /**
     * Get time clocked in seconds
     * 
     * @return
     */
    public Double getTime() {
        if (getEndDate() != null && getStartDate() != null) {
            Long msDiff = Math.abs(getEndDate().getTime() - getStartDate().getTime());
            return ((double) msDiff / (1000 * 60 * 60)) % 24;
        } else {
            return 0.0;
        }
    }

}
