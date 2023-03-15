package com.manapi.manapigateway.model.project;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.manapi.manapigateway.model.util.CrudEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Project extends CrudEntity {

    private String name;

    private String description;

    private List<ProjectRole> projectRoles;

}
