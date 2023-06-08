package com.manapi.manapigateway.model.user;

import com.manapi.manapigateway.model.subscription.Plan;
import com.manapi.manapigateway.model.subscription.Subscription;
import com.manapi.manapigateway.model.util.CrudEntity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends CrudEntity {

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String country;

    private Long failedRetries;

    private List<Subscription> subscriptions;

    /**
     * Get valid subscription
     * @return
     */
    public Subscription getValidSubscription() {

        // get subscription with valid end date
        Subscription s = subscriptions.stream()
                .filter(x -> x.getEndDate() == null || x.getEndDate().after(new Date()))
                .findFirst()
                .orElse(null);

        // if subscription is not founded
        if(s==null) {
            return new Subscription().getDefaulSubscription();
        }

        return s;
    }

    /**
     * Get feature groups
     * 
     * @return
     */
    public List<String> getActiveFeatureGroups() {
        return getValidSubscription().getFeatureGroups();
    }

    /**
     * Get all features from all feature groups
     * 
     * @return
     */
    public List<String> getActiveFeatures() {
        return getActiveFeatureGroups().stream()
            .flatMap(x -> new Subscription().getFeaturesFromGroup(x).stream())
            .distinct()
            .toList();
    }
    

    /**
     * Get plan
     * 
     * @return
     */
    public Plan getActualPlan() {

        // find valid subscription
        Subscription s = getValidSubscription();

        // if exists: get plan -> if not: default plan
        Plan plan = new Plan();
        if (s == null || s.getPlan()==null) {
            return plan.getPlan("free");
        } else {
            return plan.getPlan(s.getPlan());
        }
    }

}
