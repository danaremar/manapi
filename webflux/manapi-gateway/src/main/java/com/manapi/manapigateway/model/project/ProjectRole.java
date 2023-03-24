package com.manapi.manapigateway.model.project;

import com.manapi.manapigateway.model.util.CrudEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectRole extends CrudEntity {

    private String userId;

    private String role;

    private Boolean accepted;

}
