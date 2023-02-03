package com.manapi.manapigateway.model.users;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.manapi.manapicommon.model.users.FeatureType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Subscription {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Enumerated(EnumType.STRING)
    private FeatureType featureType;

    @NotNull
    @PastOrPresent
    private Date start;

    @NotNull
    private Date end;

    @Min(value = 0)
    @Max(value = 1000)
    private Double price;
    
}
