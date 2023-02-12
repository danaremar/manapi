package com.manapi.manapigateway.model.subscription;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private Date startDate;

    private Date endDate;

    private Date creationDate;

    private String plan;

    private List<String> featureGroups;

}
