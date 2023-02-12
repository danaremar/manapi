package com.manapi.manapigateway.model.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    private String type;

    private Long rate;

    private Long rateUnit;

    private Long cuota;

    private Long cuotaLimit;

    private Long price;
    
}
