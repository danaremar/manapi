package com.manapi.manapigateway.model.util;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrudEntity {

    @Id
    private String id;

    private Date creationDate;

    private Date modificationDate;

    private Date deleteDate;

    private Boolean active;
    
}
